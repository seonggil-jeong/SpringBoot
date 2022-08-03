package com.example.restfulservice.factory;

import com.example.restfulservice.factory.dto.CloudDto;
import com.example.restfulservice.factory.service.CloudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CloudController {

    private final CloudFactory cloudFactory;

    @GetMapping("/files/{cloudType}")
    public ResponseEntity<CloudDto> findFile(@PathVariable final CloudType cloudType) {
        CloudService cloudService = cloudFactory.findService(cloudType);

        CloudDto cloudDto = cloudService.findFile();

        log.info("cloudDto : " + cloudDto);

        return ResponseEntity.ok().body(cloudDto);
    }
}
