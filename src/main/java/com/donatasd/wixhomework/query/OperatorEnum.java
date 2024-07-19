package com.donatasd.wixhomework.query;

import java.util.Arrays;

public enum OperatorEnum {
    Equal("EQUAL"),
    LessThan("LESS_THAN"),
    GreaterThan("GREATER_THAN"),
    And("AND"),
    Or("OR"),
    Not("NOT");

    public final String value;

    OperatorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OperatorEnum valueOfString(String value) {
        return Arrays.stream(OperatorEnum.values())
                .filter((operatorEnum) -> operatorEnum.getValue().equals(value))
                .findFirst()
                .orElseThrow();
    }
}