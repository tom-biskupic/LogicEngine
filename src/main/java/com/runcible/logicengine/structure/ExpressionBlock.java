package com.runcible.logicengine.structure;

import java.util.Vector;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.BooleanExpressionVisitor;

/**
 * A class for manipulating a set of expressions together.
 * The class defined here is used to manage sets of related
 * expressions - such as an expression for each bit of a
 * binary quantity at once.
 * 
 * @author tom
 *
 */
public class ExpressionBlock
{
    /**
     * Creates an empty ExprBlock of the size specified.
     * @param size the size of the block
     */
    public ExpressionBlock(int size)
    {
        expressions = new Vector<BooleanExpression>(size,1);
        expressions.setSize(size);
    }
    
    /**
     * Returns nth output expression. Defaults to returning the input
     * @param i the index of the expression to retrieve
     * @return the expression at that index
     */
    public BooleanExpression getExpression(int i)
    {
        return expressions.get(i);
    }

    /**
     * Sets the value of the expression at the index specified
     * @param n the index of the expression
     * @param value the new expression to set at that index
     */
    public void setExpression(int n, BooleanExpression value)
    {
        expressions.set(n, value);
    }
    
    public void setExpressions( ExpressionBlock source ) throws ExpressionBlockSizeError
    {
        if ( source.size() > size() )
        {
            throw new ExpressionBlockSizeError("Cannot assign: souce size ("+source.size()+" to block of size"+size());
        }
        
        for(int i=0;i<source.size();i++)
        {
            setExpression(i,source.getExpression(i));
        }
    }
    
    public void setExpressions( ExpressionBlock source, int offset ) throws ExpressionBlockSizeError
    {
        if ( (source.size() + offset ) > size() )
        {
            throw new ExpressionBlockSizeError(
                    "Cannot assign: souce size ("+source.size()+" with offset "+offset+" to block of size"+size());
        }

        for(int i=offset,j=0;j<source.size();i++,j++)
        {
            setExpression(i,source.getExpression(j));
        }

    }
    
    /**
     * Returns a sub-block of expressions from index start to end
     * @param start the start index
     * @param end the end index
     * @return an expression block containing expressions from start to end
     */
    public ExpressionBlock getSlice(int start,int numExprs)
    {
        ExpressionBlock newBlock = new ExpressionBlock(numExprs);
        
        for(int i=start,j=0;j<numExprs;i++,j++)
        {
            newBlock.setExpression(j, getExpression(i));
        }
        
        return newBlock;
    }

    public int size()
    {
        return expressions.size();
    }
    
    public void apply(BooleanExpressionVisitor visitor)
    {
        int i=0;
        for( BooleanExpression e : expressions )
        {
            e.apply(visitor);
            System.out.println("Completed expression "+i++);
        }
    }

    protected Vector<BooleanExpression> expressions;
}
