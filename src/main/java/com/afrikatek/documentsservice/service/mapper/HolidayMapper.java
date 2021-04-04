package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.HolidayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Holiday} and its DTO {@link HolidayDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HolidayMapper extends EntityMapper<HolidayDTO, Holiday> {}
