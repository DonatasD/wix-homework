package com.donatasd.wixhomework.query;

import java.util.List;

public interface IOperator {
    public OperatorEnum getOperatorEnum();

    public String getProperty();

    public Object getValue();

    public List<IOperator> getNestedOperators();
}
