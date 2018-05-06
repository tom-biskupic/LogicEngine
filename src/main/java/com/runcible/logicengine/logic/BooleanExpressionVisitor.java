package com.runcible.logicengine.logic;

/**
 * Interface for implementations of a visitor. 
 * @author tom
 */
public interface BooleanExpressionVisitor
{
	/**
	 * Implement this interface to have the visitor process
	 * the expression passed in
	 * @param expression The expression to be visited.
	 */
    public void visit(BooleanExpression expression);
}
