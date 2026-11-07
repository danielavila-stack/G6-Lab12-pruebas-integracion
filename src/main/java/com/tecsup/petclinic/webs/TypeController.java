package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints REST para los tipos de mascota (/types).
 */
@RestController
@Slf4j
public class TypeController {

	private final TypeService typeService;

	public TypeController(TypeService typeService) {
		this.typeService = typeService;
	}

	/**
	 * GET /types            -> todos los tipos
	 * GET /types?active=true -> solo activos (o inactivos con false)
	 */
	@GetMapping(value = "/types")
	public ResponseEntity<List<TypeDTO>> findAllTypes(@RequestParam(required = false) Boolean active) {
		List<TypeDTO> types = (active == null)
				? typeService.findAll()
				: typeService.findByActive(active);
		return ResponseEntity.ok(types);
	}

	/**
	 * GET /types/{id}
	 */
	@GetMapping(value = "/types/{id}")
	public ResponseEntity<TypeDTO> findById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(typeService.findById(id));
		} catch (TypeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * POST /types
	 */
	@PostMapping(value = "/types")
	public ResponseEntity<TypeDTO> create(@RequestBody TypeDTO typeDTO) {
		TypeDTO newType = typeService.create(typeDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(newType);
	}

	/**
	 * DELETE /types/{id}
	 */
	@DeleteMapping(value = "/types/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		try {
			typeService.delete(id);
			return ResponseEntity.ok(" Delete ID :" + id);
		} catch (TypeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * GET /types/report/pet-count -> {"cat": 4, "dog": 4, ...}
	 */
	@GetMapping(value = "/types/report/pet-count")
	public ResponseEntity<Map<String, Long>> getPetCountByType() {
		return ResponseEntity.ok(typeService.getPetCountByType());
	}

}
