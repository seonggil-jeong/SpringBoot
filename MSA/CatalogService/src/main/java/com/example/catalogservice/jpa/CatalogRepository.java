package com.example.catalogservice.jpa;

import com.example.catalogservice.jpa.entity.CatalogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("CatalogRepository")
public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {

    CatalogEntity findByProductId(String productId);

    CatalogEntity findByProductSeq(int productSeq);

}
