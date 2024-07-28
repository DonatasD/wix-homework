package com.donatasd.wixhomework.query.operator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperatorFactory implements IOperatorFactory {
    @Override
    public IOperator createLogicalOperator(OperatorType type, List<IOperator> nestedOperators) {
        return new Operator(type, nestedOperators);
    }

    @Override
    public IOperator createComparatorOperator(OperatorType type, String property, Object value) {
        return new Operator(type, property, value);
    }
}
