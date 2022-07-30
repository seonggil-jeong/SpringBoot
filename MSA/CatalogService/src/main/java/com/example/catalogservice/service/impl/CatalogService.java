package com.example.catalogservice.service.impl;

import com.example.catalogservice.dto.CatalogDTO;
import com.example.catalogservice.jpa.CatalogRepository;
import com.example.catalogservice.jpa.entity.CatalogEntity;
import com.example.catalogservice.service.ICatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        if (productSeq < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "없는 상품 번호 입니다");
        }
        return catalogRepository.findByProductSeq(productSeq);
    }

    @Override
    public CatalogEntity saveCatalogInfo(CatalogEntity catalogEntity) throws Exception {
        log.info(this.getClass().getName() + "saveCatalogInfo Start");

        if (catalogEntity.getProductId() == null || catalogEntity.getProductName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "정보 없음");
        }

        log.info(this.getClass().getName() + "saveCatalogInfo End");
        return catalogRepository.save(catalogEntity);

    }
}
