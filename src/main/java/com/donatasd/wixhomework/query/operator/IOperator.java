package com.donatasd.wixhomework.query.operator;

import java.util.List;

public interface IOperator {
    public OperatorType getOperatorEnum();

    public String getProperty();

    public Object getValue();

    public List<IOperator> getNestedOperators();
}
