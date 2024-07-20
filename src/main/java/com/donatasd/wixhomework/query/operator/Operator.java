package com.donatasd.wixhomework.query.operator;


import java.util.List;
import java.util.Objects;

public class Operator implements IOperator {

    private final OperatorType type;

    private final List<IOperator> nestedOperators;

    private final String property;

    private final Object value;

    /**
     * Constructor to generate operator when operator nesting is needed.
     *
     * @param type            - {@link OperatorType} indicating operator type
     * @param nestedOperators - List of operators, with 1 or 2 elements of {@link IOperator}.
     *                        {@link OperatorType#And} and {@link OperatorType#Or} supports 2 elements.
     *                        {@link OperatorType#Not} supports 1 element.
     *                        {@link OperatorType#GreaterThan}, {@link OperatorType#LessThan} and {@link OperatorType#Equal} doesn't support nested operators.
     */
    Operator(OperatorType type, List<IOperator> nestedOperators) {
        Operator.validate(type, nestedOperators, null, null);
        this.type = type;
        this.nestedOperators = nestedOperators;
        this.property = null;
        this.value = null;
    }

    /**
     * Constructor to generate simple operator without nested operators
     *
     * @param type - {@link OperatorType} indicating operation type
     * @param property - Operator property on which operation will be applied, i.e. "id"
     * @param value    - Operator value on which operation will be evaluated, i.e. "my-store", 100
     */
    Operator(OperatorType type, String property, Object value) {
        Operator.validate(type, null, property, value);
        this.type = type;
        this.nestedOperators = null;
        this.property = property;
        this.value = value;
    }

    @Override
    public OperatorType getOperatorEnum() {
        return this.type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator1 = (Operator) o;
        return type == operator1.type && Objects.equals(nestedOperators, operator1.nestedOperators) && Objects.equals(property, operator1.property) && Objects.equals(value, operator1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, nestedOperators, property, value);
    }

    @Override
    public String toString() {
        return "Operator{" +
                "type=" + type +
                ", nestedOperators=" + nestedOperators +
                ", property='" + property + '\'' +
                ", value=" + value +
                '}';
    }

    private static void validate(OperatorType operatorType, List<IOperator> nestedOperators, String property, Object value) {
        Operator.validateNestedOperators(operatorType, nestedOperators);
        Operator.validatePropertyAndValue(operatorType, property, value);
    }

    private static void validateNestedOperators(OperatorType operatorType, List<IOperator> nestedOperators) {
        switch (operatorType) {
            case GreaterThan, LessThan, Equal -> {
                if (nestedOperators != null && !nestedOperators.isEmpty()) {
                    throw new IllegalArgumentException(operatorType.getValue() + " does not support nested operators");
                }
            }
            case Not -> {
                if (nestedOperators == null || nestedOperators.size() != 1) {
                    throw new IllegalArgumentException(operatorType.getValue() + " supports exactly 1 nested operator");
                }
            }
            case Or, And -> {
                if (nestedOperators == null || nestedOperators.size() != 2) {
                    throw new IllegalArgumentException(operatorType.getValue() + " supports exactly 2 nested operator");
                }
            }
        }
    }

    private static void validatePropertyAndValue(OperatorType operatorType, String property, Object value) {
        switch (operatorType) {
            case Not, Or, And -> {
                if (property != null || value != null) {
                    throw new IllegalArgumentException(operatorType.getValue() + " does not support property or value");
                }
            }
            case Equal, GreaterThan, LessThan -> {
                if (property == null || value == null) {
                    throw new IllegalArgumentException(operatorType.getValue() + " must contain property and value information");
                }
            }
        }
    }
}

