package com.afrikatek.documentsservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentSlotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentSlotDTO.class);
        AppointmentSlotDTO appointmentSlotDTO1 = new AppointmentSlotDTO();
        appointmentSlotDTO1.setId(1L);
        AppointmentSlotDTO appointmentSlotDTO2 = new AppointmentSlotDTO();
        assertThat(appointmentSlotDTO1).isNotEqualTo(appointmentSlotDTO2);
        appointmentSlotDTO2.setId(appointmentSlotDTO1.getId());
        assertThat(appointmentSlotDTO1).isEqualTo(appointmentSlotDTO2);
        appointmentSlotDTO2.setId(2L);
        assertThat(appointmentSlotDTO1).isNotEqualTo(appointmentSlotDTO2);
        appointmentSlotDTO1.setId(null);
        assertThat(appointmentSlotDTO1).isNotEqualTo(appointmentSlotDTO2);
    }
}
