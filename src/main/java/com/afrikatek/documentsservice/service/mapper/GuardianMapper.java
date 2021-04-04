package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.GuardianDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Guardian} and its DTO {@link GuardianDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GuardianMapper extends EntityMapper<GuardianDTO, Guardian> {
    @Named("fullName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    GuardianDTO toDtoFullName(Guardian guardian);
}
