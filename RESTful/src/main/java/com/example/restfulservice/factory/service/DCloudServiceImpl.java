package com.example.restfulservice.factory.service;

import com.example.restfulservice.factory.CloudType;
import com.example.restfulservice.factory.dto.CloudDto;
import com.example.restfulservice.factory.dto.DCloudDto;
import com.example.restfulservice.factory.dto.GCloudDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DCloudServiceImpl implements CloudService {
    @Override
    public boolean supports(CloudType cloudType) {
        return cloudType == CloudType.D;
    }

    @Override
    public CloudDto findFile() {
        log.info("findFile in D Cloud");

        DCloudDto dCloudDto = new DCloudDto();
        dCloudDto.setFileName("File in D Cloud");
        dCloudDto.setFileUrl("/dcloud.com");
        dCloudDto.setDCloudPassword(1234);

        return dCloudDto;
    }

    @Override
    public void saveFile(CloudDto cloudDto) {
        log.info("save in D Cloud Service");

    }
}
