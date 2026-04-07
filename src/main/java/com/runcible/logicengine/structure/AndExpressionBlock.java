package com.runcible.logicengine.structure;

import com.runcible.logicengine.logic.BooleanExpression;

/**
 * Bitwise AND of two expression blocks of equal size.
 */
public class AndExpressionBlock extends ExpressionBlock
{
    public AndExpressionBlock(ExpressionBlock a, ExpressionBlock b) throws ExpressionBlockSizeError
    {
        super(a.size());

        if (a.size() != b.size())
        {
            throw new ExpressionBlockSizeError(
                "Left source = " + a.size() + " Right source = " + b.size());
        }

        for (int i = 0; i < a.size(); i++)
        {
            setExpression(i, BooleanExpression.makeAnd(a.getExpression(i), b.getExpression(i)));
        }
    }
}
