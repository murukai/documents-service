package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.AppointmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppointmentDTO toDtoId(Appointment appointment);
}
