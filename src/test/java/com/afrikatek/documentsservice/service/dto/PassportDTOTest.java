package com.afrikatek.documentsservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PassportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PassportDTO.class);
        PassportDTO passportDTO1 = new PassportDTO();
        passportDTO1.setId(1L);
        PassportDTO passportDTO2 = new PassportDTO();
        assertThat(passportDTO1).isNotEqualTo(passportDTO2);
        passportDTO2.setId(passportDTO1.getId());
        assertThat(passportDTO1).isEqualTo(passportDTO2);
        passportDTO2.setId(2L);
        assertThat(passportDTO1).isNotEqualTo(passportDTO2);
        passportDTO1.setId(null);
        assertThat(passportDTO1).isNotEqualTo(passportDTO2);
    }
}
