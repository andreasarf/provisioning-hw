package com.voxloud.provisioning.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "provisioning")
public class ProvisioningProperties {

    private String domain;
    private Integer port;
    private List<String> codecs;
}
