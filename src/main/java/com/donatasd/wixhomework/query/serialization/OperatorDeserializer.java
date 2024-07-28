package com.donatasd.wixhomework.query.serialization;

import com.donatasd.wixhomework.query.operator.IOperator;
import com.donatasd.wixhomework.query.operator.IOperatorFactory;
import com.donatasd.wixhomework.query.operator.OperatorFactory;
import com.donatasd.wixhomework.query.operator.OperatorType;
import org.springframework.lang.NonNull;

import java.util.List;

public class OperatorDeserializer implements IOperatorDeserializer {

    private final IOperatorFactory operatorFactory = new OperatorFactory();

    @Override
    public IOperator deserialize(String query) {
        // Find operator information start and end points based on "(" and ")"
        var operatorStart = query.indexOf("(");
        var operatorEnd = query.lastIndexOf(")");
        if (operatorStart == -1 || operatorEnd == -1) {
            throw new IllegalArgumentException("Failed to deserialize provided query at: " + query);
        }

        var operatorEnum = OperatorType.valueOfString(query.subSequence(0, operatorStart).toString());
        var remaining = query.subSequence(operatorStart + 1, operatorEnd).toString();
        // Check if we need to parse any nested operators. String should contain "(" or ")" if that is the case.
        if (remaining.contains("(") || remaining.contains(")")) {
            List<IOperator> nestedOperators = this.deserializeNestedOperators(remaining);
            return operatorFactory.createLogicalOperator(operatorEnum, nestedOperators);
        } else {
            var array = remaining.split(",");
            return operatorFactory.createComparatorOperator(operatorEnum, array[0], OperatorDeserializer.deserializeValue(array[1]));
        }
    }

    private List<IOperator> deserializeNestedOperators(@NonNull String query) {
        if (query.contains("),")) {
            var openingAndClosingBracesPositions = findFirstOpeningAndRelatedClosingBracesPositions(query);
            var openingBracesPosition = openingAndClosingBracesPositions.get(0);
            var closingBracesPosition = openingAndClosingBracesPositions.get(1);

            if (openingBracesPosition == -1 || closingBracesPosition == -1) {
                throw new IllegalArgumentException("Failed to deserialized provided query (Missing braces) at: " + query);
            }

            var firstOperatorStringStartingPosition = 0;
            var firstOperatorStringEndPosition = closingBracesPosition + openingBracesPosition + 2;
            var firstOperatorString = query.subSequence(firstOperatorStringStartingPosition, firstOperatorStringEndPosition).toString();

            var secondOperatorStringStartPosition = firstOperatorStringEndPosition + 1;
            var secondOperatorStringEndPosition = query.length();
            var secondOperatorString = query.subSequence(secondOperatorStringStartPosition, secondOperatorStringEndPosition).toString();

            return List.of(this.deserialize(firstOperatorString), this.deserialize(secondOperatorString));
        } else {
            return List.of(this.deserialize(query));
        }
    }

    /**
     * Converts provided value to Specific type. Currently, supports only transformation to String and Integer
     *
     * @param value
     * @return
     */
    private static Object deserializeValue(String value) {
        if (value.contains("\"")) {
            return value.replaceAll("\"", "");
        }
        return Integer.valueOf(value);
    }

    /**
     * Finds first opening braces and their related closing braces. Provides a list where index 0 contains opening
     * brace position and index 1 contains related closing brace position. If braces are not found this is indicated by
     * value -1.
     *
     * @param string - {@link String} to search on
     * @return List of opening and closing brace positions. Index 0 - starting position, Index 1 - ending position
     */
    private static List<Integer> findFirstOpeningAndRelatedClosingBracesPositions(String string) {
        int openingBracesPosition = string.indexOf('(');
        int closingBracesPosition = -1;
        // Indicates open braces count to handle nested braces
        int currentOpeningBracesCount = 1;
        var remainingString = string.subSequence(openingBracesPosition + 1, string.length()).toString();

        for (int i = 0; i < remainingString.length(); i++) {
            char currentCharacter = remainingString.charAt(i);
            if (currentCharacter == '(') {
                currentOpeningBracesCount++;
            }
            if (currentCharacter == ')') {
                currentOpeningBracesCount--;
            }
            if (currentOpeningBracesCount == 0) {
                closingBracesPosition = i;
                return List.of(openingBracesPosition, closingBracesPosition);
            }
        }
        return List.of(openingBracesPosition, closingBracesPosition);
    }
}
