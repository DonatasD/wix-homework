package com.donatasd.wixhomework.store;

import com.donatasd.wixhomework.query.jpa.QuerySpecification;
import com.donatasd.wixhomework.query.operator.IOperatorFactory;
import com.donatasd.wixhomework.query.operator.OperatorFactory;
import com.donatasd.wixhomework.query.operator.OperatorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class StoreServiceTests {

    @Autowired
    private StoreService service;

    @Nested
    @DisplayName("findAll")
    public class findAllTests {

        private final IOperatorFactory operatorFactory = new OperatorFactory();

        @ParameterizedTest
        @EnumSource(names = {"Equal", "GreaterThan", "LessThan"})
        void shouldSuccessfullyHandleSimpleComparatorOperators(OperatorType operatorType) {
            var operator = operatorFactory.createComparatorOperator(operatorType, "views", 100);
            var result = service.findAll(new QuerySpecification<Store>(operator));

            Assertions.assertNotNull(result);
        }

        @ParameterizedTest
        @EnumSource(names = {"Or", "And"})
        void shouldSuccessfullyHandleSimpleLogicalOperatorWithTwoNestedParameters(OperatorType operatorType) {
            var operator = operatorFactory.createLogicalOperator(
                    operatorType,
                    List.of(
                            operatorFactory.createComparatorOperator(OperatorType.Equal, "id", "my-story"),
                            operatorFactory.createComparatorOperator(OperatorType.LessThan, "views", 100)
                    )
            );
            var result = service.findAll(new QuerySpecification<Store>(operator));

            Assertions.assertNotNull(result);
        }

        @ParameterizedTest
        @EnumSource(names = {"Not"})
        void shouldSuccessfullyHandleSimpleLogicalOperatorWithOneNestedParameter(OperatorType operatorType) {
            var operator = operatorFactory.createLogicalOperator(
                    operatorType,
                    List.of(
                            operatorFactory.createComparatorOperator(OperatorType.Equal, "id", "my-story")
                    )
            );
            var result = service.findAll(new QuerySpecification<Store>(operator));

            Assertions.assertNotNull(result);
        }
    }
}
