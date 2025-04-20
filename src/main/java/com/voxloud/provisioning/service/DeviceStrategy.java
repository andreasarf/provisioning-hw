package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;

public interface DeviceStrategy {

    String constructFile(Device device);
}
