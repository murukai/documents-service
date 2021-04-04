package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.DemographicDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DemographicDetails} and its DTO {@link DemographicDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DemographicDetailsMapper extends EntityMapper<DemographicDetailsDTO, DemographicDetails> {
    @Named("profession")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "profession", source = "profession")
    DemographicDetailsDTO toDtoProfession(DemographicDetails demographicDetails);
}
