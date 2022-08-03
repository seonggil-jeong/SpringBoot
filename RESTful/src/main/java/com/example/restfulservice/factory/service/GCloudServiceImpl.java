package com.example.restfulservice.factory.service;

import com.example.restfulservice.factory.CloudType;
import com.example.restfulservice.factory.dto.CloudDto;
import com.example.restfulservice.factory.dto.GCloudDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GCloudServiceImpl implements CloudService {
    @Override
    public boolean supports(CloudType cloudType) {
        return cloudType == CloudType.G;
    }

    @Override
    public GCloudDto findFile() {
        log.info("findFile in G Cloud");

        GCloudDto gCloudDto = new GCloudDto();
        gCloudDto.setFileName("File in G Cloud");
        gCloudDto.setFileUrl("/gcloud.com");
        gCloudDto.setGCloudPassword("1234");
        gCloudDto.setGCloudPassword2("12341234");


        return gCloudDto;

    }

    @Override
    public void saveFile(CloudDto cloudDto) {
        log.info("save in G Cloud Service");

    }
}
