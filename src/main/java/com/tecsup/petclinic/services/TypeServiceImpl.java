package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mapper.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación del servicio de tipos.
 */
@Service
@Slf4j
public class TypeServiceImpl implements TypeService {

	TypeRepository typeRepository;
	TypeMapper typeMapper;

	public TypeServiceImpl(TypeRepository typeRepository, TypeMapper typeMapper) {
		this.typeRepository = typeRepository;
		this.typeMapper = typeMapper;
	}

	@Override
	public TypeDTO create(TypeDTO typeDTO) {
		if (typeDTO.getActive() == null) {
			typeDTO.setActive(true);   // por defecto un tipo nuevo está activo
		}
		PetType newType = typeRepository.save(typeMapper.mapToEntity(typeDTO));
		return typeMapper.mapToDto(newType);
	}

	@Override
	public void delete(Integer id) throws TypeNotFoundException {
		TypeDTO type = findById(id);
		typeRepository.deleteById(type.getId());
	}

	@Override
	public TypeDTO findById(Integer id) throws TypeNotFoundException {
		Optional<PetType> type = typeRepository.findById(id);
		if (type.isEmpty())
			throw new TypeNotFoundException("Record not found...!");
		return typeMapper.mapToDto(type.get());
	}

	@Override
	public List<TypeDTO> findAll() {
		return typeMapper.mapToDtoList(typeRepository.findAll());
	}

	@Override
	public List<TypeDTO> findByActive(Boolean active) {
		return typeMapper.mapToDtoList(typeRepository.findByActive(active));
	}

	@Override
	public Map<String, Long> getPetCountByType() {
		Map<String, Long> report = new LinkedHashMap<>();
		for (Object[] row : typeRepository.countPetsByActiveType()) {
			report.put((String) row[0], (Long) row[1]);
		}
		log.info("Pet count by type: {}", report);
		return report;
	}

}
