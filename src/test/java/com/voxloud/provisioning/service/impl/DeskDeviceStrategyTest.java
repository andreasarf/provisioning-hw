package com.voxloud.provisioning.service.impl;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.ProvisioningException;
import com.voxloud.provisioning.properties.ProvisioningProperties;
import com.voxloud.provisioning.util.CustomPropertiesMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig({DeskDeviceStrategy.class, JacksonAutoConfiguration.class, CustomPropertiesMapper.class})
@EnableConfigurationProperties(ProvisioningProperties.class)
@TestPropertySource(properties = {
        "provisioning.domain=sip.voxloud.com",
        "provisioning.port=5160",
        "provisioning.codecs=G722,PCMU,PCMA"
})
class DeskDeviceStrategyTest {

    @Autowired
    private DeskDeviceStrategy strategy;
    @MockitoSpyBean
    private ProvisioningProperties provisioningProperties;

    @Test
    void givenNoOverrideFragment_whenConstructFile_thenShouldReturnStandardProvisioning() {
        // arrange
        Device device = new Device();
        device.setMacAddress("00:00:00:00:00:12");
        device.setUsername("walter");
        device.setPassword("white");
        device.setModel(Device.DeviceModel.DESK);
        String expected = """
                username=walter
                password=white
                domain=sip.voxloud.com
                port=5160
                codecs=G722,PCMU,PCMA
                """;

        // act
        String result = strategy.constructFile(device);

        // assert
        assertEquals(expected, result);
    }

    @Test
    void givenOverrideFragment_whenConstructFile_thenShouldReturnOverriddenProvisioning() {
        // arrange
        Device device = new Device();
        device.setMacAddress("00:00:00:00:00:12");
        device.setUsername("walter");
        device.setPassword("white");
        device.setModel(Device.DeviceModel.DESK);
        device.setOverrideFragment("domain=sip.anotherdomain.com\nport=5161\ntimeout=10");
        String expected = """
                username=walter
                password=white
                domain=sip.anotherdomain.com
                port=5161
                codecs=G722,PCMU,PCMA
                timeout=10
                """;

        // act
        String result = strategy.constructFile(device);

        // assert
        assertEquals(expected, result);
    }

    @Test
    void givenError_whenConstructFile_thenShouldThrowException() {
        // arrange
        Device device = new Device();
        device.setMacAddress("00:00:00:00:00:12");
        device.setUsername("walter");
        device.setPassword("white");
        device.setModel(Device.DeviceModel.DESK);
        device.setOverrideFragment("\"domain:\"sip.anotherdomain.com\",\"port\":\"5161\",\"timeout\":10");

        // act & assert
        assertThrows(ProvisioningException.class, () -> strategy.constructFile(device));
    }
}
