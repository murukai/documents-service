package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicantMapper.class })
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {
    @Mapping(target = "applicant", source = "applicant", qualifiedByName = "surname")
    CountryDTO toDto(Country s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "countryName", source = "countryName")
    CountryDTO toDtoCountryName(Country country);
}
