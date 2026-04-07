package com.runcible.logicengine.structure;

import com.runcible.logicengine.logic.BooleanExpression;

/**
 * Bitwise NOT of an expression block.
 */
public class NotExpressionBlock extends ExpressionBlock
{
    public NotExpressionBlock(ExpressionBlock source)
    {
        super(source.size());

        for (int i = 0; i < source.size(); i++)
        {
            setExpression(i, BooleanExpression.makeNot(source.getExpression(i)));
        }
    }
}
