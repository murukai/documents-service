package com.afrikatek.documentsservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DemographicDetailsMapperTest {

    private DemographicDetailsMapper demographicDetailsMapper;

    @BeforeEach
    public void setUp() {
        demographicDetailsMapper = new DemographicDetailsMapperImpl();
    }
}
