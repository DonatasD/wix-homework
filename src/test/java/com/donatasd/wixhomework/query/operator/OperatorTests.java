package com.donatasd.wixhomework.query.operator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OperatorTests {

    @Nested
    @DisplayName("constructor")
    class valueOfLabelTests {
        @ParameterizedTest
        @EnumSource(names = {"Equal", "GreaterThan", "LessThan"})
        void shouldCorrectlyCreateNonNestedOperators(OperatorType operatorType) {
            var property = "views";
            var value = 100;
            var operator = new Operator(operatorType, property, value);
            assertEquals(value, operator.getValue());
            assertEquals(property, operator.getProperty());
            assertNull(operator.getNestedOperators());
            assertEquals(operatorType, operator.getOperatorEnum());
        }

        @ParameterizedTest
        @EnumSource(names = {"Not"})
        void shouldCorrectlyCreateOperatorsWithSingleNestedOperator(OperatorType operatorType) {
            List<IOperator> nestedOperator = List.of(new Operator(OperatorType.Equal, "id", "firstPost"));
            var operator = new Operator(operatorType, nestedOperator);
            assertNull(operator.getValue());
            assertNull(operator.getProperty());
            assertEquals(nestedOperator, operator.getNestedOperators());
            assertEquals(operatorType, operator.getOperatorEnum());
        }

        @ParameterizedTest
        @EnumSource(names = {"And", "Or"})
        void shouldCorrectlyCreateOperatorsWithMultipleNestedOperators(OperatorType operatorType) {
            List<IOperator> nestedOperators = List.of(
                    new Operator(OperatorType.Equal, "id", "firstPost"),
                    new Operator(OperatorType.GreaterThan, "views", 100)
            );
            var operator = new Operator(operatorType, nestedOperators);

            assertNull(operator.getValue());
            assertNull(operator.getProperty());
            assertEquals(nestedOperators, operator.getNestedOperators());
            assertEquals(operatorType, operator.getOperatorEnum());
        }

        @Test
        void shouldSuccessfullyCreateComplexNestedOperator() {
            var nestedOperator1 = new Operator(
                    OperatorType.Or,
                    List.of(
                            new Operator(OperatorType.GreaterThan, "views", 100),
                            new Operator(OperatorType.LessThan, "timestamp", Instant.now().getEpochSecond())
                    )
            );

            var nestedOperator2 = new Operator(
                    OperatorType.And,
                    List.of(
                            new Operator(OperatorType.LessThan, "views", 100),
                            new Operator(
                                    OperatorType.Not,
                                    List.of(new Operator(OperatorType.Equal, "id", "my-store")
                                    ))
                    )
            );
            List<IOperator> nestedOperators = List.of(nestedOperator1, nestedOperator2);
            var operatorEnum = OperatorType.And;
            var operator = new Operator(operatorEnum, nestedOperators);

            assertNull(operator.getValue());
            assertNull(operator.getProperty());
            assertEquals(nestedOperators, operator.getNestedOperators());
            assertEquals(operatorEnum, operator.getOperatorEnum());
        }

        @ParameterizedTest
        @EnumSource(names = {"Equal", "GreaterThan", "LessThan"})
        void shouldFailWhenCreatingWithNestedOperatorsIsNotSupported(OperatorType operatorType) {
            List<IOperator> nestedOperator = List.of(new Operator(OperatorType.Equal, "views", 100));
            assertThrows(IllegalArgumentException.class, () -> new Operator(operatorType, nestedOperator));
        }

        @ParameterizedTest
        @EnumSource(names = {"Or", "And"})
        void shouldFailWhenExactlyTwoOperatorsAreSupportedAndLessIsProvided(OperatorType operatorType) {
            List<IOperator> nestedOperator = List.of(new Operator(OperatorType.Equal, "views", 100));
            assertThrows(IllegalArgumentException.class, () -> new Operator(operatorType, nestedOperator));
        }

        @ParameterizedTest
        @EnumSource(names = {"Not"})
        void shouldFailWhenExactlyOneOperatorIsSupportedAndLessIsProvided(OperatorType operatorType) {
            assertThrows(IllegalArgumentException.class, () -> new Operator(operatorType, null));
        }
    }
}
