package com.afrikatek.documentsservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PassportMapperTest {

    private PassportMapper passportMapper;

    @BeforeEach
    public void setUp() {
        passportMapper = new PassportMapperImpl();
    }
}
