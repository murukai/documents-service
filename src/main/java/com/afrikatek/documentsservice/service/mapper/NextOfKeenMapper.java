package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.NextOfKeenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NextOfKeen} and its DTO {@link NextOfKeenDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicantMapper.class, AddressMapper.class })
public interface NextOfKeenMapper extends EntityMapper<NextOfKeenDTO, NextOfKeen> {
    @Mapping(target = "applicant", source = "applicant", qualifiedByName = "surname")
    @Mapping(target = "address", source = "address", qualifiedByName = "city")
    NextOfKeenDTO toDto(NextOfKeen s);
}
