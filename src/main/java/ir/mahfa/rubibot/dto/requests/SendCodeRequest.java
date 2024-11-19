package ir.mahfa.rubibot.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SendCodeRequest {
    private String phoneNumber;
    private String sendType;
    private String passKey;
}
