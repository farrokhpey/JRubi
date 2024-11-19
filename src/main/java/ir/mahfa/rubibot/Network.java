package ir.mahfa.rubibot;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ir.mahfa.rubibot.dto.ClientDTO;
import ir.mahfa.rubibot.dto.Helper;
import ir.mahfa.rubibot.dto.SessionData;
import ir.mahfa.rubibot.dto.requests.InnerData;
import ir.mahfa.rubibot.dto.requests.SendDataRequest;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Network {
    private final Methods methods;

    public Network(Methods methods) {
        this.methods = methods;
    }

    public String request(String method, Object data, boolean tempSession) {
        String url = Helper.getApiServer();
        String platform = methods.getPlatform().toLowerCase();
        int apiVersion = methods.getApiVersion();
        ClientDTO clientDTO;
        if (List.of("rubx", "rubikax").contains(platform)) {
            clientDTO = ClientDTO.builder()
                    .app_name("Main")
                    .app_version("3.5.7")
                    .lang_code("fa")
                    ._package("ir.rubx.bapp")
                    .temp_code("27")
                    .platform("Android")
                    .build();

        } else if (platform.equals("android")) {
            clientDTO = ClientDTO.builder()
                    .app_name("Main")
                    .app_version("3.5.7")
                    .lang_code("fa")
                    ._package("app.rbmain.a")
                    .temp_code("27")
                    .platform("Android")
                    .build();
        } else {
            clientDTO = ClientDTO.builder()
                    .app_name("Main")
                    .app_version("4.4.6")
                    .lang_code("fa")
                    ._package("web.rubika.ir")
                    .platform("Web")
                    .build();
        }

        SendDataRequest.SendDataRequestBuilder sendDataRequestBuilder = SendDataRequest.builder();

        Crypto crypto = new Crypto();
        SessionData sessionData = new SessionData();

        sendDataRequestBuilder.api_version(String.valueOf(apiVersion));
        if (tempSession) {
            sendDataRequestBuilder.tmp_session(crypto.auth);
        } else {
            if (apiVersion > 5) {
                sendDataRequestBuilder.auth(crypto.changeAuthType(sessionData.getAuth()));
            } else {
                sendDataRequestBuilder.auth(sessionData.getAuth());
            }
        }

        InnerData innerData = InnerData.builder()
                .method(method)
                .input(data)
                .client(clientDTO)
                .build();

        Gson gson = new Gson();
        String json = gson.toJson(innerData);
        String encryptedData = crypto.encrypt(json);

        sendDataRequestBuilder.data_enc(encryptedData);

        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://web.rubika.ir/");
        headers.put("Content-Type", "application/json; charset=utf-8");

        if (!tempSession && apiVersion > 5) {
            sendDataRequestBuilder.sign(crypto.makeSignFromData(encryptedData));
        }
        int maxAttempt = 3;
        int attempt = 0;
        while (attempt <= maxAttempt) {
            var result = post(
                    url,
                    headers,
                    "dumps(data).encode()",
                    methods.getTimeOut()
            );
            try {
                JsonObject decryptedResult = new Gson().fromJson(crypto.decrypt(result), JsonObject.class);
                JsonObject dataEnc = decryptedResult.getAsJsonObject("data_enc");
                JsonObject decryptedData = new Gson().fromJson(crypto.decrypt(dataEnc.toString()), JsonObject.class);
                if ("OK" .equals(decryptedData.get("status").getAsString())) {
                    if (tempSession) {
                        decryptedData.getAsJsonObject("data").addProperty("tmp_session", crypto.auth);
                    }
                    System.out.println(decryptedData.getAsJsonObject("data"));
                    break;
                } else {
                    throw new Exception(decryptedData.get("status_det").getAsString());
                }
            } catch (Exception e) {
                attempt++;
                if (attempt > maxAttempt) {
                    e.printStackTrace();
                    break;
                }
                System.out.println("Retrying... Attempt " + attempt);
            }
            attempt++;
        }
        return "";
    }

    private String post(String url, Map<String, String> headers, String dataToSend, int timeOut) {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(dataToSend, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = Objects.requireNonNull(response.body()).string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject data = jsonResponse.getAsJsonObject("data");
                return data.getAsString();
            } else {
                System.out.println("Request failed: " + response.code());
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}