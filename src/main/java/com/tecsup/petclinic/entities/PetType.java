package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la tabla "types" (tipos de mascota).
 *
 * @author jgomezm
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "types")
@Data
public class PetType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "size_category")
	private String sizeCategory;

	@Column(name = "average_lifespan")
	private Integer averageLifespan;

	@Column(name = "care_level")
	private String careLevel;

}
