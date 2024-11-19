package ir.mahfa.rubibot;

import ir.mahfa.rubibot.dto.SendCodeData;
import ir.mahfa.rubibot.dto.SessionData;
import ir.mahfa.rubibot.dto.requests.SendCodeRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class Methods {
    Network network;
    private final String platform;
    private final int apiVersion;
    private final int timeOut;
    public Methods(SessionData sessionData,
                   String platform,
                   int apiVersion,
                   String proxy,
                   int timeOut,
                   boolean showProgressBar) {

        this.platform = platform;
        this.apiVersion = apiVersion;
        this.timeOut = timeOut;
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
