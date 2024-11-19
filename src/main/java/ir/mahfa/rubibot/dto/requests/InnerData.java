package ir.mahfa.rubibot.dto.requests;

import ir.mahfa.rubibot.dto.ClientDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InnerData {
    private String method;
    private Object input;
    private ClientDTO client;
}
