package com.example.catalogservice.controller;

import com.example.catalogservice.jpa.entity.CatalogEntity;
import com.example.catalogservice.service.impl.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
    @Resource(name = "CatalogService")
    private CatalogService catalogService;

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        log.info(this.getClass().getName() + ".getCatalogs Start!");

        Iterable<CatalogEntity> rList = catalogService.getAllCatalogs();
        ModelMapper mapper = new ModelMapper();
        List<ResponseCatalog> result = new ArrayList<>();
        rList.forEach(v -> {
            result.add(mapper.map(v, ResponseCatalog.class));
        });

        log.info(this.getClass().getName() + ".getCatalogs End!");

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/catalogs/{id}")
    public ResponseEntity<Map<String, Object>> getCatalogsBySeq(@PathVariable int id) {
        log.info(this.getClass().getName() + ".getCatalogsBySeq Start!");
        log.info("Catalog seq : " + String.valueOf(id));
        Map<String, Object> rMap = new HashMap<>();

        CatalogEntity catalogEntity = catalogService.getOneCatalogBySeq(id);

        ModelMapper mapper = new ModelMapper();
        ResponseCatalog rResponse = mapper.map(catalogEntity, ResponseCatalog.class);
        EntityModel<ResponseCatalog> model = EntityModel.of(rResponse);

        model.add(linkTo(methodOn(this.getClass()).getCatalogs()).withRel("catalogs"));

        rMap.put("result", model);
        rMap.put("id", id);

        return ResponseEntity.status(HttpStatus.OK).body(rMap);
    }
}
