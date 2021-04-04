package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.AppointmentSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppointmentSettings} and its DTO {@link AppointmentSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppointmentSettingsMapper extends EntityMapper<AppointmentSettingsDTO, AppointmentSettings> {}
