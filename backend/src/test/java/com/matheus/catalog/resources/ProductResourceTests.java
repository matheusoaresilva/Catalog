package com.matheus.catalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheus.catalog.dto.ProductDTO;
import com.matheus.catalog.services.ProductService;
import com.matheus.catalog.services.exceptions.DatabaseException;
import com.matheus.catalog.services.exceptions.ResourceNotFoundException;
import com.matheus.catalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService service;

    @Autowired
    ObjectMapper objectMapper;

    Long existingId;
    Long nonExistingId;

    Long dependentId;

    ProductDTO productDTO;
    PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.insert(any())).thenReturn(productDTO);

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);

        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);

        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existingId);

        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);

        doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .contentType((MediaType.APPLICATION_JSON)));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/products/{id}", existingId)
                        .contentType((MediaType.APPLICATION_JSON)));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(post("/products")
                        .content(jsonBody)
                        .contentType((MediaType.APPLICATION_JSON))
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                                .content(jsonBody)
                                .contentType((MediaType.APPLICATION_JSON))
                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk());
                result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
                result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
                result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdDoesNotExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType((MediaType.APPLICATION_JSON))
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{
        ResultActions result =
                mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk());
                result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
                result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
                result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdDoesNotExist() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}

