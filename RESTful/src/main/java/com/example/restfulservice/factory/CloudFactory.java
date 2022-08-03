package com.example.restfulservice.factory;

import com.example.restfulservice.factory.service.CloudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class CloudFactory {

    private final List<CloudService> cloudServiceList;
    private final Map<CloudType, CloudService> factoryCache;


    public CloudService findService(final CloudType cloudType) {

        CloudService cloudService = factoryCache.get(cloudType);

        if (cloudService != null) {
            return cloudService;
        }
        cloudService = cloudServiceList.stream()
                .filter(s -> s.supports(cloudType))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        factoryCache.put(cloudType, cloudService);
        return cloudService;
    }
}
