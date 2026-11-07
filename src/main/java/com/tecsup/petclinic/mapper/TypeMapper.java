package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.PetType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Convierte PetType (entidad) <-> TypeDTO. MapStruct genera el código.
 */
@Mapper(componentModel = "spring")
public interface TypeMapper {

	TypeMapper INSTANCE = Mappers.getMapper(TypeMapper.class);

	PetType mapToEntity(TypeDTO typeDTO);

	TypeDTO mapToDto(PetType type);

	List<TypeDTO> mapToDtoList(List<PetType> typeList);

	List<PetType> mapToEntityList(List<TypeDTO> typeDTOList);

}
