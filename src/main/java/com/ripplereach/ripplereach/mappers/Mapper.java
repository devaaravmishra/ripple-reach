package com.ripplereach.ripplereach.mappers;

public interface Mapper<A, B> {
    public A mapFrom(B b);
    public B mapTo(A a);
}
