package com.donatasd.wixhomework.query.jpa;

import com.donatasd.wixhomework.query.operator.IOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class QuerySpecification<T> implements Specification<T> {

    private final IOperator operator;

    public QuerySpecification(IOperator operator) {
        this.operator = operator;
    }

    @Override
    @Nullable
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        if (operator == null) {
            return null;
        }

        switch (operator.getOperatorEnum()) {
            case Equal -> {
                return criteriaBuilder.equal(root.get(operator.getProperty()), operator.getValue().toString());
            }
            case GreaterThan -> {
                return criteriaBuilder.greaterThan(root.get(operator.getProperty()), operator.getValue().toString());
            }
            case LessThan -> {
                return criteriaBuilder.lessThan(root.get(operator.getProperty()), operator.getValue().toString());
            }
            case And -> {
                return criteriaBuilder.and(
                        operator.getNestedOperators()
                                .stream()
                                .map((nestedOperator) -> new QuerySpecification<T>(nestedOperator).toPredicate(root, query, criteriaBuilder))
                                .toArray(Predicate[]::new)
                );
            }
            case Or -> {
                return criteriaBuilder.or(
                        operator.getNestedOperators()
                                .stream()
                                .map((nestedOperator) -> new QuerySpecification<T>(nestedOperator).toPredicate(root, query, criteriaBuilder))
                                .toArray(Predicate[]::new)
                );
            }
            case Not -> {
                return criteriaBuilder.not(
                        new QuerySpecification<T>(operator.getNestedOperators().get(0)).toPredicate(root, query, criteriaBuilder)
                );
            }
            default -> {
                throw new UnsupportedOperationException();
            }
        }
    }
}
