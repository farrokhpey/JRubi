package ir.mahfa.rubibot;

import ir.mahfa.rubibot.dto.SendCodeData;
import ir.mahfa.rubibot.dto.SessionData;
import ir.mahfa.rubibot.dto.requests.SendCodeRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
public class Methods {
    private final String platform;
    private final int apiVersion;
    private final int timeOut;
    private final String proxy;
    private final boolean showProgressBar;
    private final SessionData sessionData;
    private final Network network;
    private final Cryption crypto;

    public Methods(SessionData sessionData,
                   String platform,
                   int apiVersion,
                   String proxy,
                   int timeOut,
                   boolean showProgressBar) throws Exception {

        this.platform = platform.toLowerCase();
        if (!List.of("android", "web", "rubx", "rubikax", "rubino").contains(platform)) {
            throw new Exception("The \"{" + platform + "}\" is not a valid platform. Choose these one -> (web, android, rubx)");
        }
        this.apiVersion = apiVersion;
        this.timeOut = timeOut;
        this.proxy = proxy;
        this.showProgressBar = showProgressBar;
        this.sessionData = sessionData;
        if (sessionData != null) {
            this.crypto = new Cryption(sessionData.getAuth(), sessionData.getPrivate_key())
        } else {
            this.crypto = new Cryption(Utils.randomTmpSession(), null);
        }
        this.network = new Network(this);
    }

    public SendCodeData sendCode(String phoneNumber, String passKey, boolean sendInternal) {
        SendCodeRequest input = SendCodeRequest.builder()
                .phoneNumber("98" + Utils.phoneNumberParse(phoneNumber))
                .sendType(sendInternal ? "Internal" : "SMS")
                .build();

        if (StringUtils.isNotEmpty(passKey)) {
            input.setPassKey(passKey);
        }
        String result = network.request("sendCode", input, true);
        return null;
    }
}
