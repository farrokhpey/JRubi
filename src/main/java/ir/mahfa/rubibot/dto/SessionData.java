package ir.mahfa.rubibot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SessionData {
    private String auth;
    private String private_key;

    public SessionData() {
    }
}
