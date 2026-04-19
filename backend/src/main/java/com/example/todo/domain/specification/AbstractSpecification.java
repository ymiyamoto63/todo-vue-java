package com.example.todo.domain.specification;

/**
 * Specificationの基底抽象クラス。
 * サブクラスは isSatisfiedBy() だけを実装すればよい。
 * and/or/not の組み合わせロジックはここで提供する。
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

    @Override
    public Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    @Override
    public Specification<T> not() {
        return new NotSpecification<>(this);
    }
}
