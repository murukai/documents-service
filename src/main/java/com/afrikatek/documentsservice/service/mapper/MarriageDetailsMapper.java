package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.MarriageDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MarriageDetails} and its DTO {@link MarriageDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicantMapper.class })
public interface MarriageDetailsMapper extends EntityMapper<MarriageDetailsDTO, MarriageDetails> {
    @Mapping(target = "applicant", source = "applicant", qualifiedByName = "surname")
    MarriageDetailsDTO toDto(MarriageDetails s);
}
