package com.runcible.logicengine.logic;

/**
 * Interface for implementations of a visitor. 
 * @author tom
 *
 */
public interface BooleanExpressionVisitor
{
    public void visit(BooleanExpression expression);
}
