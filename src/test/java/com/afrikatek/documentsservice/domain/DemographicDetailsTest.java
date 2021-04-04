package com.afrikatek.documentsservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemographicDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemographicDetails.class);
        DemographicDetails demographicDetails1 = new DemographicDetails();
        demographicDetails1.setId(1L);
        DemographicDetails demographicDetails2 = new DemographicDetails();
        demographicDetails2.setId(demographicDetails1.getId());
        assertThat(demographicDetails1).isEqualTo(demographicDetails2);
        demographicDetails2.setId(2L);
        assertThat(demographicDetails1).isNotEqualTo(demographicDetails2);
        demographicDetails1.setId(null);
        assertThat(demographicDetails1).isNotEqualTo(demographicDetails2);
    }
}
