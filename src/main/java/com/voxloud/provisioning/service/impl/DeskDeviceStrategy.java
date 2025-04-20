package com.voxloud.provisioning.service.impl;

import com.voxloud.provisioning.dto.ProvisioningDto;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.ProvisioningException;
import com.voxloud.provisioning.properties.ProvisioningProperties;
import com.voxloud.provisioning.util.CustomPropertiesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service("DESK")
@RequiredArgsConstructor
@EnableConfigurationProperties(ProvisioningProperties.class)
public class DeskDeviceStrategy extends BaseDeviceStrategy {

    private final CustomPropertiesMapper propsMapper;

    @Override
    public String constructFile(Device device) {
        try {
            final var provisioning = getBasicProvisioning(device);

            if (device.getOverrideFragment() != null) {
                final var overrideFragment = propsMapper.deserialize(device.getOverrideFragment(), ProvisioningDto.class);
                checkForOverriddenFragment(provisioning, overrideFragment);
            }

            return propsMapper.serialize(provisioning);
        } catch (Exception e) {
            log.error("Error while constructing desk provisioning file for device {}", device.getMacAddress(), e);
            throw new ProvisioningException("Error when constructing desk provisioning file", e);
        }
    }

}
