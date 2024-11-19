package ir.mahfa.rubibot.dto;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Helper {

    public List<String> getDcmess() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://getdcmess.iranlms.ir/")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = Objects.requireNonNull(response.body()).string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject data = jsonResponse.getAsJsonObject("data");
            } else {
                System.out.println("Request failed: " + response.code());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getApiServer() {
        Random rand = new Random();
        int randomNum = rand.nextInt(2) + 2;
        return String.format("https://messengerg2c%d.iranlms.ir", randomNum);
    }

    public String getSocketServer() {
        Random random = new Random();
        List<String> socketList = getDcmess();
        return socketList.get(random.nextInt(socketList.size()));
    }
}