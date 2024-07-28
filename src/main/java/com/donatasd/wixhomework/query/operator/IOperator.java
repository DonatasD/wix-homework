package com.donatasd.wixhomework.query.operator;

import java.util.List;

public interface IOperator {
    OperatorType getOperatorEnum();

    String getProperty();

    Object getValue();

    List<IOperator> getNestedOperators();
}
