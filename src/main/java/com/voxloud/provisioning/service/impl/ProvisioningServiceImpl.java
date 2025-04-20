package com.voxloud.provisioning.service.impl;

import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.service.DeviceStrategy;
import com.voxloud.provisioning.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;
    private final Map<String, DeviceStrategy> deviceStrategies;

    public String getProvisioningFile(String macAddress) {
        return deviceRepository.findByMacAddress(macAddress)
                .map(device -> deviceStrategies.get(device.getModel().name()).constructFile(device))
                .orElse(null);
    }
}
