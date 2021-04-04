package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.DeclarationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Declaration} and its DTO {@link DeclarationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeclarationMapper extends EntityMapper<DeclarationDTO, Declaration> {
    @Named("passportNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "passportNumber", source = "passportNumber")
    DeclarationDTO toDtoPassportNumber(Declaration declaration);
}
