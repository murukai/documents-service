package com.afrikatek.documentsservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeclarationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeclarationDTO.class);
        DeclarationDTO declarationDTO1 = new DeclarationDTO();
        declarationDTO1.setId(1L);
        DeclarationDTO declarationDTO2 = new DeclarationDTO();
        assertThat(declarationDTO1).isNotEqualTo(declarationDTO2);
        declarationDTO2.setId(declarationDTO1.getId());
        assertThat(declarationDTO1).isEqualTo(declarationDTO2);
        declarationDTO2.setId(2L);
        assertThat(declarationDTO1).isNotEqualTo(declarationDTO2);
        declarationDTO1.setId(null);
        assertThat(declarationDTO1).isNotEqualTo(declarationDTO2);
    }
}
