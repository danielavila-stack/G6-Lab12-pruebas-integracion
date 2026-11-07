package com.tecsup.petclinic.util;

import com.tecsup.petclinic.dtos.TypeDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Crea objetos de prueba para los tests con Mockito.
 * Los datos son los mismos que carga data.sql.
 */
public class TObjectCreator {

	// Tipo con id = 1 (cat)
	public static TypeDTO getTypeTO() {
		return TypeDTO.builder()
				.id(1).name("cat").description("Domestic feline").active(true)
				.sizeCategory("small").averageLifespan(15).careLevel("medium")
				.build();
	}

	// Los 8 tipos de data.sql
	public static List<TypeDTO> getAllTypesTO() {
		return List.of(
				TypeDTO.builder().id(1).name("cat").description("Domestic feline").active(true).sizeCategory("small").averageLifespan(15).careLevel("medium").build(),
				TypeDTO.builder().id(2).name("dog").description("Domestic canine").active(true).sizeCategory("medium").averageLifespan(12).careLevel("medium").build(),
				TypeDTO.builder().id(3).name("lizard").description("Reptile").active(true).sizeCategory("small").averageLifespan(10).careLevel("high").build(),
				TypeDTO.builder().id(4).name("bird").description("Avian").active(true).sizeCategory("small").averageLifespan(8).careLevel("medium").build(),
				TypeDTO.builder().id(5).name("hamster").description("Small rodent").active(true).sizeCategory("small").averageLifespan(3).careLevel("low").build(),
				TypeDTO.builder().id(6).name("snake").description("Serpent").active(false).sizeCategory("medium").averageLifespan(20).careLevel("high").build(),
				TypeDTO.builder().id(7).name("rabbit").description("Domestic rabbit").active(true).sizeCategory("small").averageLifespan(9).careLevel("medium").build(),
				TypeDTO.builder().id(8).name("turtle").description("Turtle or tortoise").active(true).sizeCategory("small").averageLifespan(30).careLevel("medium").build()
		);
	}

	// Solo los activos (7, sin snake)
	public static List<TypeDTO> getActiveTypesTO() {
		return getAllTypesTO().stream().filter(TypeDTO::getActive).toList();
	}

	// Tipo nuevo para crear (sin id)
	public static TypeDTO newTypeTO() {
		return TypeDTO.builder()
				.name("ferret").description("Small mustelid").active(true)
				.sizeCategory("small").averageLifespan(8).careLevel("medium")
				.build();
	}

	// Tipo nuevo ya creado (con id asignado)
	public static TypeDTO newTypeTOCreated() {
		TypeDTO type = newTypeTO();
		type.setId(100);
		return type;
	}

	// Reporte de mascotas por tipo (solo activos)
	public static Map<String, Long> getPetCountByType() {
		Map<String, Long> report = new LinkedHashMap<>();
		report.put("bird", 1L);
		report.put("cat", 4L);
		report.put("dog", 4L);
		report.put("hamster", 2L);
		report.put("lizard", 1L);
		return report;
	}

}
