package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integracion real de TypeController (/types) contra H2
 * Grupo 6
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    // ===================== Integrante C : Filtros y errores =====================

    /**
     * GET /types?active=true -> 7 tipos (excluye snake)
     */
    @Test
    public void testFindActiveTypes() throws Exception {

        int NRO_ACTIVE_TYPES = 7;

        this.mockMvc.perform(get("/types").param("active", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(NRO_ACTIVE_TYPES)))
                .andExpect(jsonPath("$[*].name", not(hasItem("snake"))));
    }

}
