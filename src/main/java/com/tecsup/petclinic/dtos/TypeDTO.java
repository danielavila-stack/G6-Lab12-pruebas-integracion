package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto que viaja en el JSON de los endpoints /types.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TypeDTO {

	private Integer id;

	private String name;

	private String description;

	private Boolean active;

	private String sizeCategory;

	private Integer averageLifespan;

	private String careLevel;

}
