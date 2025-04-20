package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProvisioningController {

    private final ProvisioningService provisioningService;

    @GetMapping(path = "/provisioning/{macAddress}", produces = "text/plain")
    public ResponseEntity<InputStreamResource> getProvisioningFile(@PathVariable String macAddress) {
        log.info("API Get Provisioning for macAddress: {}", macAddress);

        final var config = provisioningService.getProvisioningFile(macAddress);

        if (StringUtils.isBlank(config)) {
            return ResponseEntity.notFound().build();
        }

        final var configBytes = config.getBytes(StandardCharsets.UTF_8);
        final var inputStream = new ByteArrayInputStream(configBytes);

        final var headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("file.config")
                .build());

        final var resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
