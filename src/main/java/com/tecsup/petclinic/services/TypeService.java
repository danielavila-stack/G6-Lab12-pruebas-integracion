package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * Operaciones disponibles sobre los tipos de mascota.
 */
public interface TypeService {

	TypeDTO create(TypeDTO typeDTO);

	void delete(Integer id) throws TypeNotFoundException;

	TypeDTO findById(Integer id) throws TypeNotFoundException;

	List<TypeDTO> findAll();

	List<TypeDTO> findByActive(Boolean active);

	/**
	 * Reporte: nombre del tipo -> cantidad de mascotas (excluye tipos inactivos)
	 */
	Map<String, Long> getPetCountByType();

}
