package com.example.catalogservice.service.impl;

import com.example.catalogservice.jpa.CatalogRepository;
import com.example.catalogservice.jpa.entity.CatalogEntity;
import com.example.catalogservice.service.ICatalogService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;

@Data
@Slf4j
@Service("CatalogService")
public class CatalogService implements ICatalogService {

    @Resource(name = "CatalogRepository")
    private CatalogRepository catalogRepository;


    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {

        return catalogRepository.findAll();
    }

    @Override // seq 로 상품 조회하기
    public CatalogEntity getOneCatalogBySeq(int productSeq) {
        return catalogRepository.findByProductSeq(productSeq);
    }
}
