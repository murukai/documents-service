package com.afrikatek.documentsservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemographicDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemographicDetailsDTO.class);
        DemographicDetailsDTO demographicDetailsDTO1 = new DemographicDetailsDTO();
        demographicDetailsDTO1.setId(1L);
        DemographicDetailsDTO demographicDetailsDTO2 = new DemographicDetailsDTO();
        assertThat(demographicDetailsDTO1).isNotEqualTo(demographicDetailsDTO2);
        demographicDetailsDTO2.setId(demographicDetailsDTO1.getId());
        assertThat(demographicDetailsDTO1).isEqualTo(demographicDetailsDTO2);
        demographicDetailsDTO2.setId(2L);
        assertThat(demographicDetailsDTO1).isNotEqualTo(demographicDetailsDTO2);
        demographicDetailsDTO1.setId(null);
        assertThat(demographicDetailsDTO1).isNotEqualTo(demographicDetailsDTO2);
    }
}
