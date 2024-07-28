package com.donatasd.wixhomework.query.serialization;

import com.donatasd.wixhomework.query.operator.OperatorFactory;
import com.donatasd.wixhomework.query.operator.OperatorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OperatorDeserializerTests {

    @Autowired
    private OperatorDeserializer deserializer;

    @Autowired
    private OperatorFactory factory;

    @Nested
    @DisplayName("convert")
    public class convertTests {

        @Test
        void shouldConvertStringToEqualOperationSuccessfully() {
            var expected = factory.createComparatorOperator(OperatorType.Equal, "id", "first-post");
            var input = "EQUAL(id,\"first-post\")";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldConvertStringToAndOperationSuccessfully() {
            var expected = factory.createLogicalOperator(OperatorType.And, List.of(
                    factory.createComparatorOperator(OperatorType.Equal, "id", "first-post"),
                    factory.createComparatorOperator(OperatorType.Equal, "views", 100)
            ));
            var input = "AND(EQUAL(id,\"first-post\"),EQUAL(views,100))";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldConvertStringToOrOperationSuccessfully() {
            var expected = factory.createLogicalOperator(OperatorType.Or, List.of(
                    factory.createComparatorOperator(OperatorType.Equal, "id", "first-post"),
                    factory.createComparatorOperator(OperatorType.Equal, "id", "second-post")
            ));
            var input = "OR(EQUAL(id,\"first-post\"),EQUAL(id,\"second-post\"))";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldConvertStringToNotOperationSuccessfully() {
            var expected = factory.createLogicalOperator(OperatorType.Not, List.of(
                    factory.createComparatorOperator(OperatorType.Equal, "id", "first-post")
            ));
            var input = "NOT(EQUAL(id,\"first-post\"))";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldConvertStringToGreaterThanOperationSuccessfully() {
            var expected = factory.createComparatorOperator(OperatorType.GreaterThan, "views", 100);
            var input = "GREATER_THAN(views,100)";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldConvertStringToLessThanOperationSuccessfully() {
            var expected = factory.createComparatorOperator(OperatorType.LessThan, "views", 100);
            var input = "LESS_THAN(views,100)";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldConvertStringToComplexOperationSuccessfully() {
            var expected = factory.createLogicalOperator(OperatorType.And, List.of(
                    factory.createLogicalOperator(
                            OperatorType.Or,
                            List.of(
                                    factory.createComparatorOperator(OperatorType.GreaterThan, "views", 100),
                                    factory.createComparatorOperator(OperatorType.LessThan, "timestamp", 123456789)
                            )
                    ),
                    factory.createLogicalOperator(
                            OperatorType.And,
                            List.of(
                                    factory.createComparatorOperator(OperatorType.LessThan, "views", 100),
                                    factory.createLogicalOperator(
                                            OperatorType.Not,
                                            List.of(factory.createComparatorOperator(OperatorType.Equal, "id", "my-store")
                                            ))
                            )
                    )
            ));
            var input =
                    "AND(" +
                            "OR(" +
                            "GREATER_THAN(views,100)," +
                            "LESS_THAN(timestamp,123456789)" +
                            ")," +
                            "AND(" +
                            "LESS_THAN(views,100)," +
                            "NOT(" +
                            "EQUAL(id,\"my-store\")" +
                            ")" +
                            ")" +
                            ")";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldConvertStringToComplexOperationSuccessfully2() {
            var expected = factory.createLogicalOperator(OperatorType.And, List.of(
                    factory.createLogicalOperator(
                            OperatorType.Or,
                            List.of(
                                    factory.createComparatorOperator(OperatorType.GreaterThan, "views", 100),
                                    factory.createLogicalOperator(OperatorType.And, List.of(
                                            factory.createLogicalOperator(OperatorType.Not, List.of(
                                                    factory.createComparatorOperator(OperatorType.Equal, "views", 20)
                                            )),
                                            factory.createLogicalOperator(OperatorType.Or, List.of(
                                                    factory.createComparatorOperator(OperatorType.LessThan, "views", 2000),
                                                    factory.createComparatorOperator(OperatorType.Equal, "views", 9999)
                                            ))
                                    ))
                            )
                    ),
                    factory.createLogicalOperator(
                            OperatorType.And,
                            List.of(
                                    factory.createLogicalOperator(OperatorType.Or, List.of(
                                            factory.createComparatorOperator(OperatorType.Equal, "id", "pet-store"),
                                            factory.createLogicalOperator(OperatorType.Or, List.of(
                                                    factory.createComparatorOperator(OperatorType.Equal, "id", "my-store"),
                                                    factory.createLogicalOperator(OperatorType.Not, List.of(
                                                            factory.createComparatorOperator(OperatorType.Equal, "id", "pet-store")
                                                    ))
                                            ))
                                    )),
                                    factory.createLogicalOperator(
                                            OperatorType.Not, List.of(
                                                    factory.createComparatorOperator(OperatorType.Equal, "id", "my-store")
                                            )
                                    )
                            )
                    )
            ));
            var input =
                    "AND(" +
                            "OR(" +
                            "GREATER_THAN(views,100)," +
                            "AND(" +
                            "NOT(" +
                            "EQUAL(views,20)" +
                            ")," +
                            "OR(" +
                            "LESS_THAN(views,2000)," +
                            "EQUAL(views,9999)" +
                            ")" +
                            ")" +
                            ")," +
                            "AND(" +
                            "OR(" +
                            "EQUAL(id,\"pet-store\")," +
                            "OR(" +
                            "EQUAL(id,\"my-store\")," +
                            "NOT(" +
                            "EQUAL(id,\"pet-store\")" +
                            ")" +
                            ")" +
                            ")," +
                            "NOT(" +
                            "EQUAL(id,\"my-store\")" +
                            ")" +
                            ")" +
                            ")";

            assertEquals(expected, deserializer.deserialize(input));
        }

        @Test
        void shouldFailToConvertInvalidStringWhenClosingBraceIsMissing() {
            // Missing ending braces
            var input =
                    "OR(" +
                            "EQUAL(id,\"first-post\")," +
                            "EQUAL(id,\"second-post\")";

            assertThrows(IllegalArgumentException.class, () -> deserializer.deserialize(input));
        }
    }
}
