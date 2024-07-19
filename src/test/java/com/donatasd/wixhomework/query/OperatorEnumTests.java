package com.donatasd.wixhomework.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OperatorEnumTests {

    @Nested
    @DisplayName("valueOfString")
    class valueOfLabelTests {
        @ParameterizedTest
        @ValueSource(strings = {"EQUAL", "LESS_THAN", "GREATER_THAN", "AND", "OR", "NOT"})
        void shouldCorrectlyReturnOperatorEnum(String value) {
            assertEquals(value, OperatorEnum.valueOfString(value).getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = "INVALID")
        void shouldThrowWhenProvidingUnknownValue(String value) {
            assertThrows(IllegalArgumentException.class, () -> OperatorEnum.valueOf(value));
        }
    }
}
