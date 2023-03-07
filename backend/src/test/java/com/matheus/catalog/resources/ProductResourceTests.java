package com.matheus.catalog.resources;

import com.matheus.catalog.dto.ProductDTO;
import com.matheus.catalog.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService service;

    PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        when(service.findAllPaged(null)).thenReturn(page);
    }
    }
}
