package com.afrikatek.documentsservice.service.mapper;

import com.afrikatek.documentsservice.domain.*;
import com.afrikatek.documentsservice.service.dto.ApplicantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Applicant} and its DTO {@link ApplicantDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DemographicDetailsMapper.class, DeclarationMapper.class, GuardianMapper.class, UserMapper.class, AppointmentSlotMapper.class }
)
public interface ApplicantMapper extends EntityMapper<ApplicantDTO, Applicant> {
    @Mapping(target = "democraphicDetails", source = "democraphicDetails", qualifiedByName = "profession")
    @Mapping(target = "declaration", source = "declaration", qualifiedByName = "passportNumber")
    @Mapping(target = "guardian", source = "guardian", qualifiedByName = "fullName")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "appointmentSlot", source = "appointmentSlot", qualifiedByName = "timeOfAppointment")
    ApplicantDTO toDto(Applicant s);

    @Named("surname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "surname", source = "surname")
    ApplicantDTO toDtoSurname(Applicant applicant);
}
