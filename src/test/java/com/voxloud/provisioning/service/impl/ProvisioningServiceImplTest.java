package com.voxloud.provisioning.service.impl;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.service.DeviceStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(ProvisioningServiceImpl.class)
class ProvisioningServiceImplTest {

    @Autowired
    private ProvisioningServiceImpl service;
    @MockitoBean
    private DeviceRepository deviceRepository;
    @MockitoBean(name = "DESK")
    private DeviceStrategy deskStrategy;
    @MockitoBean(name = "CONFERENCE")
    private DeviceStrategy conferenceStrategy;

    @Test
    void givenNoData_whenGetProvisioningFile_thenShouldReturnNull() {
        // arrange
        String macAddress = "00:00:00:00:00:00";

        when(deviceRepository.findByMacAddress(Mockito.anyString())).thenReturn(Optional.empty());

        // act
        String result = service.getProvisioningFile(macAddress);

        // assert
        assertNull(result);
    }

    @Test
    void givenDeskDevice_whenGetProvisioningFile_thenShouldReturnDeskProvisioning() {
        // arrange
        String macAddress = "00:00:00:00:00:12";
        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setModel(Device.DeviceModel.DESK);
        String provision = """
                username=walter
                password=white
                domain=sip.anotherdomain.com
                port=5161
                codecs=G711,G729,OPUS
                timeout=10
                """;

        when(deviceRepository.findByMacAddress(anyString())).thenReturn(Optional.of(device));
        when(deskStrategy.constructFile(any())).thenReturn(provision);

        // act
        String result = service.getProvisioningFile(macAddress);

        // assert
        assertEquals(provision, result);
    }

    @Test
    void givenConferenceDevice_whenGetProvisioningFile_thenShouldReturnConferenceProvisioning() {
        // arrange
        String macAddress = "00:45:00:00:00:FF";
        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setModel(Device.DeviceModel.CONFERENCE);
        String provision = """
                {
                    "username" : "john",
                    "password" : "doe",
                    "domain" : "sip.voxloud.com",
                    "port" : "5060",
                    "codecs" : ["G711","G729","OPUS"]
                }
                """;

        when(deviceRepository.findByMacAddress(anyString())).thenReturn(Optional.of(device));
        when(conferenceStrategy.constructFile(any())).thenReturn(provision);

        // act
        String result = service.getProvisioningFile(macAddress);

        // assert
        assertEquals(provision, result);
    }

    @Test
    void givenDbError_whenGetProvisioningFile_thenShouldThrowException() {
        // arrange
        String macAddress = "00:45:00:00:00:FF";

        when(deviceRepository.findByMacAddress(anyString())).thenThrow(new RuntimeException());

        // act
        assertThrows(RuntimeException.class,() -> service.getProvisioningFile(macAddress));

        // assert
        verify(conferenceStrategy, never()).constructFile(any());
        verify(deskStrategy, never()).constructFile(any());
    }
}
