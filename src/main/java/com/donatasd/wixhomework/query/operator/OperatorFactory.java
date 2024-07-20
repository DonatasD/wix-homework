package com.donatasd.wixhomework.query.operator;

import java.util.List;

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
