package ir.mahfa.rubibot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientDTO {
    private String app_name;
    private String app_version;
    private String lang_code;
    @JsonProperty("package")
    private String _package;
    private String temp_code;
    private String platform;
}
