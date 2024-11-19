package ir.mahfa.rubibot.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class SendDataRequest {
    private String api_version;
    private String tmp_session;
    private String auth;
    private String method;
    private Object input;
    private String client;
    private String data_enc;
    private String sign;
}
