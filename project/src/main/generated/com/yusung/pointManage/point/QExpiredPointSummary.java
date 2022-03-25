package com.yusung.pointManage.point;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.yusung.pointManage.point.QExpiredPointSummary is a Querydsl Projection type for ExpiredPointSummary
 */
@Generated("com.querydsl.codegen.ProjectionSerializer")
public class QExpiredPointSummary extends ConstructorExpression<ExpiredPointSummary> {

    private static final long serialVersionUID = -1248874502L;

    public QExpiredPointSummary(com.querydsl.core.types.Expression<String> userId, com.querydsl.core.types.Expression<? extends java.math.BigInteger> amount) {
        super(ExpiredPointSummary.class, new Class<?>[]{String.class, java.math.BigInteger.class}, userId, amount);
    }

}

