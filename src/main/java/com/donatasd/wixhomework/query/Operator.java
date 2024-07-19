package com.donatasd.wixhomework.query;


import java.util.List;

public class Operator implements IOperator {

    private final OperatorEnum operator;

    private final List<IOperator> nestedOperators;

    private final String property;

    private final Object value;

    /**
     * Constructor to generate operator when operator nesting is needed.
     *
     * @param operator - {@link OperatorEnum} indicating operation type
     * @param nestedOperators - List of operators, with 1 or more elements of {@link IOperator}
     */
    public Operator(OperatorEnum operator, List<IOperator> nestedOperators) {
        this.operator = operator;
        this.nestedOperators = nestedOperators;
        this.property = null;
        this.value = null;
    }

    /**
     * Constructor to generate simple operator without nested operators
     *
     * @param operator - {@link OperatorEnum} indicating operation type
     * @param property - Operator property on which operation will be applied, i.e. "id"
     * @param value - Operator value on which operation will be evaluated, i.e. "my-store", 100
     */
    public Operator(OperatorEnum operator, String property, Object value) {
        this.operator = operator;
        this.nestedOperators = null;
        this.property = property;
        this.value = value;
    }

    @Override
    public OperatorEnum getOperatorEnum() {
        return this.operator;
    }

    @Override
    public String getProperty() {
        return this.property;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public List<IOperator> getNestedOperators() {
        return this.nestedOperators;
    }
}
