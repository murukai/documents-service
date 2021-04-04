package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.PassportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Passport} and its DTO {@link PassportDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PassportMapper extends EntityMapper<PassportDTO, Passport> {}
