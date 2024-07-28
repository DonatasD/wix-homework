package com.donatasd.wixhomework.query.serialization.spring;

import com.donatasd.wixhomework.query.operator.IOperator;
import com.donatasd.wixhomework.query.serialization.OperatorDeserializer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OperatorConverter implements Converter<String, IOperator> {

    private final OperatorDeserializer operatorDeserializer;

    public OperatorConverter(OperatorDeserializer operatorDeserializer) {
        this.operatorDeserializer = operatorDeserializer;
    }

    @Override
    @NonNull
    public IOperator convert(@NonNull String query) {
        return operatorDeserializer.deserialize(query);
    }
}
