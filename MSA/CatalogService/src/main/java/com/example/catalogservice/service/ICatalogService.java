package com.example.catalogservice.service;

import com.example.catalogservice.jpa.entity.CatalogEntity;

import java.util.Iterator;

public interface ICatalogService {
    Iterable<CatalogEntity> getAllCatalogs();

    CatalogEntity getOneCatalogBySeq(int productSeq);

}
