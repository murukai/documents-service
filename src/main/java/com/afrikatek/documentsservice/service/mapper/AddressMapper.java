package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class, ApplicantMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    @Mapping(target = "applicant", source = "applicant", qualifiedByName = "surname")
    AddressDTO toDto(Address s);

    @Named("city")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "city", source = "city")
    AddressDTO toDtoCity(Address address);
}
