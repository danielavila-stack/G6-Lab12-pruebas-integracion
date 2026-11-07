package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de TypeController (/types) con el servicio mockeado (@MockitoBean).
 * Solo se prueba el controlador y el contrato JSON; no se toca la BD.
 *
 * Integrante B: testCreateType, testGetPetCountByType, testDeleteType
 * Pruebas de TypeController con el servicio MOCKEADO.
 * No se toca la base de datos: Mockito decide qué responde el servicio,
 * así solo se prueba la lógica del controlador y el JSON que devuelve.
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

    // ---------------------------------------------------------------
    // Integrante B — Creación y reporte
    // ---------------------------------------------------------------

    /**
     * POST /types -> 201 Created
     */
    @Test
    public void testCreateType() throws Exception {

        TypeDTO newTypeTO = TObjectCreator.newTypeTO();
        TypeDTO createdTypeTO = TObjectCreator.newTypeTOCreated();

        Mockito.when(typeService.create(newTypeTO))
                .thenReturn(createdTypeTO);

        this.mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newTypeTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdTypeTO.getId())))
                .andExpect(jsonPath("$.name", is(createdTypeTO.getName())))
                .andExpect(jsonPath("$.description", is(createdTypeTO.getDescription())))
                .andExpect(jsonPath("$.active", is(createdTypeTO.getActive())))
                .andExpect(jsonPath("$.sizeCategory", is(createdTypeTO.getSizeCategory())))
                .andExpect(jsonPath("$.averageLifespan", is(createdTypeTO.getAverageLifespan())))
                .andExpect(jsonPath("$.careLevel", is(createdTypeTO.getCareLevel())));
    }

    /**
     * GET /types/report/pet-count -> conteo por tipo (cat = 4)
     */
    @Test
    public void testGetPetCountByType() throws Exception {

        Map<String, Long> report = TObjectCreator.getPetCountByType();

        Mockito.when(typeService.getPetCountByType())
                .thenReturn(report);

        this.mockMvc.perform(get("/types/report/pet-count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cat", is(4)))
                .andExpect(jsonPath("$.dog", is(4)))
                .andExpect(jsonPath("$.size()", is(report.size())));
    }

    /**
     * POST + DELETE /types/{id} -> 200 OK
     */
    @Test
    public void testDeleteType() throws Exception {

        // ------------ Create ---------------
        TypeDTO newTypeTO = TObjectCreator.newTypeTO();
        TypeDTO createdTypeTO = TObjectCreator.newTypeTOCreated();

        Mockito.when(typeService.create(newTypeTO))
                .thenReturn(createdTypeTO);

        ResultActions mvcActions = this.mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newTypeTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // ------------ Delete ---------------
        Mockito.doNothing().when(typeService).delete(id);

        this.mockMvc.perform(delete("/types/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(typeService).delete(id);
    }

}
	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TypeService typeService;   // servicio simulado


	/**
	 * GET /types -> 200 OK y la lista que devuelve el servicio simulado
	 */
	@Test
	public void testFindAllTypes() throws Exception {

		List<TypeDTO> typesTO = TObjectCreator.getAllTypesTO();

		Mockito.when(typeService.findAll())
				.thenReturn(typesTO);

		this.mockMvc.perform(get("/types"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(typesTO.size())))
				.andExpect(jsonPath("$[0].id", is(typesTO.get(0).getId())))
				.andExpect(jsonPath("$[0].name", is(typesTO.get(0).getName())));
	}

	/**
	 * GET /types/1 -> 200 OK con el tipo que devuelve el servicio simulado
	 */
	@Test
	public void testFindTypeOK() throws Exception {

		TypeDTO typeTO = TObjectCreator.getTypeTO();

		Mockito.when(typeService.findById(typeTO.getId()))
				.thenReturn(typeTO);

		this.mockMvc.perform(get("/types/" + typeTO.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(typeTO.getId())))
				.andExpect(jsonPath("$.name", is(typeTO.getName())))
				.andExpect(jsonPath("$.description", is(typeTO.getDescription())))
				.andExpect(jsonPath("$.active", is(typeTO.getActive())));
	}

	/**
	 * GET /types/666 -> el servicio lanza TypeNotFoundException -> 404
	 */
	@Test
	public void testFindTypeKO() throws Exception {

		Integer ID_NOT_EXIST = 666;

		Mockito.when(typeService.findById(ID_NOT_EXIST))
				.thenThrow(new TypeNotFoundException("Record not found...!"));

        this.mockMvc.perform(get("/types/" + ID_NOT_EXIST))
                .andDo(print())
                .andExpect(status().isNotFound());
	}

}
