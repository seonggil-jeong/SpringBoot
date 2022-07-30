package com.example.catalogservice.service.impl;

import com.example.catalogservice.dto.CatalogDTO;
import com.example.catalogservice.jpa.CatalogRepository;
import com.example.catalogservice.jpa.entity.CatalogEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = {CatalogService.class, CatalogRepository.class}) {same}
@ExtendWith(SpringExtension.class)
@Import({CatalogService.class, CatalogRepository.class})
class CatalogServiceTest {

    @MockBean
    private CatalogRepository catalogRepository; // Service Test 이기 때문에 Repository 는 Mock

    @Autowired
    private CatalogService catalogService; // Service 는 직접 구현한 로직을 사용

    @Test
    @Order(1)
    void test1() {

        CatalogEntity catalogEntity = new CatalogEntity();
        catalogEntity.setProductSeq(1);
        catalogEntity.setProductId("10000");
        catalogEntity.setStock(1000);
        catalogEntity.setUnitPrice(10000);

        // given
        Mockito.when(catalogRepository.findByProductSeq(10000)) // when
                .thenReturn(catalogEntity); // then return {value}

        CatalogEntity rCatalogEntity = catalogService.getOneCatalogBySeq(10000);


        // 단정문 : 테스트 케이스의 기대값에 대해 수행 결과를 확인할 수 있음
        Assertions.assertEquals(rCatalogEntity.getProductId(), "10000"); // id 값이 같은 지 확인
    }

    @Test
    @Order(2)
    @DisplayName("없는 상품 번호 요청 시 Exception Test")
    void test2() {
        CatalogEntity catalogEntity = new CatalogEntity();
        catalogEntity.setProductSeq(1);
        catalogEntity.setProductId("10000");
        catalogEntity.setStock(1000);
        catalogEntity.setUnitPrice(10000);

        Mockito.when(catalogRepository.findByProductSeq(-10))
                .thenReturn(catalogEntity);

        Throwable exception = assertThrows(ResponseStatusException.class, () -> { // Exception 이 같다면 통과

            // 로직 실행 중 Check
            CatalogEntity rCatalogEntity = catalogService.getOneCatalogBySeq(-10);
        });

        // Message equals Test
        Assertions.assertEquals("400", exception.getMessage().substring(0, 3));


    }

}