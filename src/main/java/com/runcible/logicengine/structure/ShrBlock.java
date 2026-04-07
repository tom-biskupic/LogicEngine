package com.runcible.logicengine.structure;

import com.runcible.logicengine.logic.ConstantExpression;

/**
 * Logical right shift (non-circular). Zeros fill the high (MSB) side.
 * Bit 0 is the MSB; shifting right by n moves bits toward higher indices.
 */
public class ShrBlock extends ExpressionBlock
{
    public ShrBlock(ExpressionBlock source, int shiftDistance)
    {
        super(source.size());

        // Fill high bits (positions 0..shiftDistance-1) with zero constants
        for (int i = 0; i < shiftDistance; i++)
        {
            setExpression(i, new ConstantExpression(false));
        }

        // Remaining bits shift right: source bit (i - shiftDistance) → position i
        for (int i = shiftDistance; i < source.size(); i++)
        {
            setExpression(i, source.getExpression(i - shiftDistance));
        }
    }
}
