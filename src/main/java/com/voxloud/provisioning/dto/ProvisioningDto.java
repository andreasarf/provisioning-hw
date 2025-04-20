package com.voxloud.provisioning.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvisioningDto implements Serializable {

    private String username;
    private String password;
    private String domain;
    private String port;
    private List<String> codecs;

    @JsonIgnore
    @Builder.Default
    private Map<String, Object> others = new HashMap<>();

    @JsonAnySetter
    public void setOthers(String key, Object value) {
        if (others == null) {
            others = new HashMap<>();
        }
        others.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getOthers() {
        return others;
    }
}
