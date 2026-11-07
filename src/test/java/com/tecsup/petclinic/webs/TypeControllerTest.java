package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración REAL de TypeController (sin mocks).
 * La petición recorre controlador -> servicio -> repositorio -> H2,
 * usando los datos que carga data.sql.
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;


	/**
	 * GET /types -> 200 OK y lista JSON; el primer registro es "cat" (id 1).
	 * No se valida el total (8) porque otras pruebas pueden crear tipos
	 * en la misma base H2 y el número cambiaría.
	 */
	@Test
	public void testFindAllTypes() throws Exception {

		final int ID_FIRST_RECORD = 1;
		final String NAME_FIRST_RECORD = "cat";

		this.mockMvc.perform(get("/types"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", not(empty())))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)))
				.andExpect(jsonPath("$[0].name", is(NAME_FIRST_RECORD)));

	}

}
