package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.TypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración real (sin mocks) para TypeController (/types).
 * Atraviesa controlador -> servicio -> repositorio -> H2.
 *
 * Integrante B: testCreateType, testGetPetCountByType, testDeleteType
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
// Reinicia el contexto (y por tanto schema.sql/data.sql) antes de esta clase,
// para que otros tests (ej. PetControllerTest.testCreatePet) no alteren el conteo del reporte.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class TypeControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    // ---------------------------------------------------------------
    // Integrante B — Creación y reporte
    // ---------------------------------------------------------------

    /**
     * POST /types -> 201 Created
     */
    @Test
    public void testCreateType() throws Exception {

        String TYPE_NAME = "ferret";
        String DESCRIPTION = "Small mustelid";
        String SIZE_CATEGORY = "small";
        int AVERAGE_LIFESPAN = 8;
        String CARE_LEVEL = "medium";

        TypeDTO newTypeTO = TypeDTO.builder()
                .name(TYPE_NAME)
                .description(DESCRIPTION)
                .sizeCategory(SIZE_CATEGORY)
                .averageLifespan(AVERAGE_LIFESPAN)
                .careLevel(CARE_LEVEL)
                .build();

        ResultActions mvcActions = this.mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newTypeTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(TYPE_NAME)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.active", is(true))) // el servicio lo activa por defecto
                .andExpect(jsonPath("$.sizeCategory", is(SIZE_CATEGORY)))
                .andExpect(jsonPath("$.averageLifespan", is(AVERAGE_LIFESPAN)))
                .andExpect(jsonPath("$.careLevel", is(CARE_LEVEL)));

        // Limpieza: dejamos H2 como estaba para no afectar los tests de los otros integrantes
        // (ej. testFindActiveTypes espera exactamente 7 tipos activos).
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        this.mockMvc.perform(delete("/types/" + id))
                .andExpect(status().isOk());
    }

    /**
     * GET /types/report/pet-count -> conteo de mascotas por tipo (cat = 4)
     */
    @Test
    public void testGetPetCountByType() throws Exception {

        int CAT_COUNT = 4;

        this.mockMvc.perform(get("/types/report/pet-count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cat", is(CAT_COUNT)));
    }

    /**
     * POST + DELETE /types/{id} -> 200 OK
     */
    @Test
    public void testDeleteType() throws Exception {

        String TYPE_NAME = "hedgehog";

        TypeDTO newTypeTO = TypeDTO.builder()
                .name(TYPE_NAME)
                .description("Small spiny mammal")
                .sizeCategory("small")
                .averageLifespan(6)
                .careLevel("medium")
                .build();

        // CREATE
        ResultActions mvcActions = this.mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newTypeTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // DELETE
        this.mockMvc.perform(delete("/types/" + id))
                .andExpect(status().isOk());

        // Confirmación: ya no existe
        this.mockMvc.perform(get("/types/" + id))
                .andExpect(status().isNotFound());
    }

}