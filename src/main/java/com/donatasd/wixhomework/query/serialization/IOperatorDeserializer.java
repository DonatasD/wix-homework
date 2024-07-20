package com.donatasd.wixhomework.query.serialization;

import com.donatasd.wixhomework.query.operator.IOperator;

public interface IOperatorDeserializer {
    IOperator deserialize(String string);
}
