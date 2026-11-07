package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de TypeController (/types) con el servicio mockeado
 * Grupo 6
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerMockitoTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TypeService typeService;

    // ===================== Integrante C : Filtros y errores =====================

    /**
     * GET /types?active=true -> 7 tipos (excluye snake)
     */
    @Test
    public void testFindActiveTypes() throws Exception {

        int NRO_ACTIVE_TYPES = 7;

        Mockito.when(this.typeService.findByActive(true))
                .thenReturn(TObjectCreator.getActiveTypesTO());

        this.mockMvc.perform(get("/types").param("active", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(NRO_ACTIVE_TYPES)))
                .andExpect(jsonPath("$[*].name", not(hasItem("snake"))));
    }

    /**
     * GET /types/report/pet-count -> el reporte no incluye tipos inactivos (snake)
     */
    @Test
    public void testGetPetCountByType_ExcludeInactive() throws Exception {

        Mockito.when(this.typeService.getPetCountByType())
                .thenReturn(TObjectCreator.getPetCountByType());

        this.mockMvc.perform(get("/types/report/pet-count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cat", is(4)))
                .andExpect(jsonPath("$.snake").doesNotExist());
    }

}
