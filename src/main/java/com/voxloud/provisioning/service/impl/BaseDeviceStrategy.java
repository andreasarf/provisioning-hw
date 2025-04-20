package com.voxloud.provisioning.service.impl;

import com.voxloud.provisioning.dto.ProvisioningDto;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.properties.ProvisioningProperties;
import com.voxloud.provisioning.service.DeviceStrategy;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDeviceStrategy implements DeviceStrategy {

    @Autowired
    protected ProvisioningProperties provisioningProperties;

    protected ProvisioningDto getBasicProvisioning(Device device) {
        return ProvisioningDto.builder()
                .username(device.getUsername())
                .password(device.getPassword())
                .domain(provisioningProperties.getDomain())
                .port(provisioningProperties.getPort().toString())
                .codecs(provisioningProperties.getCodecs())
                .build();
    }

    protected void checkForOverriddenFragment(ProvisioningDto provisioning, ProvisioningDto overrideFragment) {
        if (overrideFragment.getDomain() != null) {
            provisioning.setDomain(overrideFragment.getDomain());
        }
        if (overrideFragment.getPort() != null) {
            provisioning.setPort(overrideFragment.getPort());
        }
        if (overrideFragment.getCodecs() != null) {
            provisioning.setCodecs(overrideFragment.getCodecs());
        }
        provisioning.setOthers(overrideFragment.getOthers());
    }

}
