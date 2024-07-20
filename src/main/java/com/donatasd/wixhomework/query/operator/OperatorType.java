package com.donatasd.wixhomework.query.operator;

import java.util.Arrays;

public enum OperatorType {
    Equal("EQUAL"),
    LessThan("LESS_THAN"),
    GreaterThan("GREATER_THAN"),
    And("AND"),
    Or("OR"),
    Not("NOT");

    public final String value;

    OperatorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OperatorType valueOfString(String value) {
        return Arrays.stream(OperatorType.values())
                .filter((operatorEnum) -> operatorEnum.getValue().equals(value))
                .findFirst()
                .orElseThrow();
    }
}