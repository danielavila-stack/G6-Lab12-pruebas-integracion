package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Acceso a la tabla "types".
 */
@Repository
public interface TypeRepository extends JpaRepository<PetType, Integer> {

	// Tipos filtrados por activo / inactivo
	List<PetType> findByActive(Boolean active);

	// Cantidad de mascotas por tipo (solo tipos activos).
	// Cada fila: [0] = nombre del tipo, [1] = cantidad de mascotas
	@Query("SELECT t.name, COUNT(p) FROM types t, pets p " +
		   "WHERE p.typeId = t.id AND t.active = true " +
		   "GROUP BY t.name ORDER BY t.name")
	List<Object[]> countPetsByActiveType();

}
