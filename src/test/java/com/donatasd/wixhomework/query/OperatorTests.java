package com.donatasd.wixhomework.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperatorTests {

    @Nested
    @DisplayName("constructor")
    class valueOfLabelTests {
        @ParameterizedTest
        @EnumSource(names = {"Equal", "GreaterThan", "LessThan"})
        void shouldCorrectlyCreateNonNestedOperators(OperatorEnum operatorEnum) {
            var property = "views";
            var value = 100;
            var operator = new Operator(operatorEnum, property, value);
            assertEquals(value, operator.getValue());
            assertEquals(property, operator.getProperty());
            assertNull(operator.getNestedOperators());
            assertEquals(operatorEnum, operator.getOperatorEnum());
        }

        @ParameterizedTest
        @EnumSource(names = {"Not"})
        void shouldCorrectlyCreateOperatorsWithSingleNestedOperator(OperatorEnum operatorEnum) {
            List<IOperator> nestedOperator = List.of(new Operator(OperatorEnum.Equal, "id", "firstPost"));
            var operator = new Operator(operatorEnum, nestedOperator);
            assertNull(operator.getValue());
            assertNull(operator.getProperty());
            assertEquals(nestedOperator, operator.getNestedOperators());
            assertEquals(operatorEnum, operator.getOperatorEnum());
        }

        @ParameterizedTest
        @EnumSource(names = {"And", "Or"})
        void shouldCorrectlyCreateOperatorsWithMultipleNestedOperators(OperatorEnum operatorEnum) {
            List<IOperator> nestedOperators = List.of(
                    new Operator(OperatorEnum.Equal, "id", "firstPost"),
                    new Operator(OperatorEnum.GreaterThan, "views", 100)
            );
            var operator = new Operator(operatorEnum, nestedOperators);

            assertNull(operator.getValue());
            assertNull(operator.getProperty());
            assertEquals(nestedOperators, operator.getNestedOperators());
            assertEquals(operatorEnum, operator.getOperatorEnum());
        }

        @Test
        void shouldSuccessfullyCreateComplexNestedOperator() {
            var nestedOperator1 = new Operator(
                    OperatorEnum.Or,
                    List.of(
                            new Operator(OperatorEnum.GreaterThan, "views", 100),
                            new Operator(OperatorEnum.LessThan, "timestamp", Instant.now().getEpochSecond())
                    )
            );

            var nestedOperator2 = new Operator(
                    OperatorEnum.And,
                    List.of(
                            new Operator(OperatorEnum.LessThan, "views", 100),
                            new Operator(
                                    OperatorEnum.Not,
                                    List.of(new Operator(OperatorEnum.Equal, "id", "my-store")
                            ))
                    )
            );
            List<IOperator> nestedOperators = List.of(nestedOperator1, nestedOperator2);
            var operatorEnum = OperatorEnum.And;
            var operator = new Operator(operatorEnum, nestedOperators);

            assertNull(operator.getValue());
            assertNull(operator.getProperty());
            assertEquals(nestedOperators, operator.getNestedOperators());
            assertEquals(operatorEnum, operator.getOperatorEnum());
        }

    }
}
