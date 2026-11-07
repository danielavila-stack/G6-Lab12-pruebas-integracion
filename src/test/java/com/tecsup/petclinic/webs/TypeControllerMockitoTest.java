package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
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
