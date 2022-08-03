package com.example.restfulservice.factory.service;

import com.example.restfulservice.factory.CloudType;
import com.example.restfulservice.factory.dto.CloudDto;

public interface CloudService {
    boolean supports(CloudType cloudType);

    CloudDto findFile();


    void saveFile(CloudDto cloudDto);
}
