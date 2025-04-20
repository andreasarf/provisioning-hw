package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.exception.ProvisioningException;
import com.voxloud.provisioning.service.ProvisioningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProvisioningController.class)
class ProvisioningControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProvisioningService provisioningService;


    @Test
    void givenNoData_whenGetProvisioningFile_thenShouldRespond404() throws Exception {
        // arrange
        String macAddress = "01:00:00:00:00:12";

        when(provisioningService.getProvisioningFile(macAddress)).thenReturn(null);

        // act
        mockMvc.perform(get("/api/v1/provisioning/{macAddress}", macAddress))
                .andExpect(status().isNotFound());

        // assert
        verify(provisioningService).getProvisioningFile(macAddress);
    }

    @Test
    void givenDeskData_whenGetProvisioningFile_thenShouldRespondDeskProvisioning() throws Exception {
        // arrange
        String macAddress = "01:00:00:00:00:12";
        String provision = """
                username=walter
                password=white
                domain=sip.anotherdomain.com
                port=5161
                codecs=G722,PCMU,PCMA
                timeout=10
                """;

        when(provisioningService.getProvisioningFile(macAddress)).thenReturn(provision);

        // act
        mockMvc.perform(get("/api/v1/provisioning/{macAddress}", macAddress))
                .andExpect(status().isOk())
                .andExpect(content().string(provision));

        // assert
        verify(provisioningService).getProvisioningFile(macAddress);
    }

    @Test
    void givenConferenceData_whenGetProvisioningFile_thenShouldRespondConferenceProvisioning() throws Exception {
        // arrange
        String macAddress = "01:00:00:00:00:12";
        String provision = """
                {
                    "username": "walter",
                    "password": "white",
                    "domain": "sip.anotherdomain.com",
                    "port": "5161",
                    "codecs": [
                        "G722",
                        "PCMU",
                        "PCMA"
                    ]
                }""";

        when(provisioningService.getProvisioningFile(macAddress)).thenReturn(provision);

        // act
        mockMvc.perform(get("/api/v1/provisioning/{macAddress}", macAddress))
                .andExpect(status().isOk())
                .andExpect(content().json(provision));

        // assert
        verify(provisioningService).getProvisioningFile(macAddress);
    }

    @Test
    void givenException_whenGetProvisioningFile_thenShouldRespond500() throws Exception {
        // arrange
        String macAddress = "invalid_mac_address";

        when(provisioningService.getProvisioningFile(macAddress)).thenThrow(new ProvisioningException("Error"));

        // act
        mockMvc.perform(get("/api/v1/provisioning/{macAddress}", macAddress))
                .andExpect(status().is5xxServerError());

        // assert
        verify(provisioningService).getProvisioningFile(macAddress);
    }
}
