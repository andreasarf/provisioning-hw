package com.voxloud.provisioning.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.dto.ProvisioningDto;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.ProvisioningException;
import com.voxloud.provisioning.properties.ProvisioningProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service("CONFERENCE")
@RequiredArgsConstructor
@EnableConfigurationProperties(ProvisioningProperties.class)
public class ConferenceDeviceStrategy extends BaseDeviceStrategy {

    private final ObjectMapper objectMapper;

    @Override
    public String constructFile(Device device) {
        try {
            final var provisioning = getBasicProvisioning(device);

            if (device.getOverrideFragment() != null) {
                final var overrideFragment = objectMapper.readValue(device.getOverrideFragment(), ProvisioningDto.class);
                checkForOverriddenFragment(provisioning, overrideFragment);
            }

            return objectMapper.writeValueAsString(provisioning);
        } catch (Exception e) {
            log.error("Error while constructing conference provisioning file for device {}", device.getMacAddress(), e);
            throw new ProvisioningException("Error when constructing conference provisioning file", e);
        }
    }
}
