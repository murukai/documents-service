package com.afrikatek.documentsservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeclarationMapperTest {

    private DeclarationMapper declarationMapper;

    @BeforeEach
    public void setUp() {
        declarationMapper = new DeclarationMapperImpl();
    }
}
