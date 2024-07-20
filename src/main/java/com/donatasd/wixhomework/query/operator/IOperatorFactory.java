package com.donatasd.wixhomework.query.operator;

import java.util.List;

public interface IOperatorFactory {

    IOperator createLogicalOperator(OperatorType type, List<IOperator> nestedOperators);

    IOperator createComparatorOperator(OperatorType type, String property, Object value);
}
