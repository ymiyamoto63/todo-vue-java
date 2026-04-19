package com.example.todo.domain.specification;

/**
 * Specificationパターンの基底インターフェース。
 * ビジネスルールをオブジェクトとして表現し、and/or/not で組み合わせ可能にする。
 */
public interface Specification<T> {

    boolean isSatisfiedBy(T candidate);

    Specification<T> and(Specification<T> other);

    Specification<T> or(Specification<T> other);

    Specification<T> not();
}
