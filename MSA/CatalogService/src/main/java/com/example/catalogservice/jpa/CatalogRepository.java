package com.example.catalogservice.jpa;

import com.example.catalogservice.jpa.entity.CatalogEntity;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
    CatalogEntity findByProductId(String productId);

}
