package com.donatasd.wixhomework.query.operator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OperatorTypeTests {

    @Nested
    @DisplayName("valueOfString")
    class valueOfLabelTests {
        @ParameterizedTest
        @ValueSource(strings = {"EQUAL", "LESS_THAN", "GREATER_THAN", "AND", "OR", "NOT"})
        void shouldCorrectlyReturnOperatorEnum(String value) {
            assertEquals(value, OperatorType.valueOfString(value).getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = "INVALID")
        void shouldThrowWhenProvidingUnknownValue(String value) {
            assertThrows(IllegalArgumentException.class, () -> OperatorType.valueOf(value));
        }
    }
}
