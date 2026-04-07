package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.ShiftBlock;
import com.runcible.logicengine.structure.XorExpressionBlock;

/**
 * SHA-256 upper-case Sigma-0: Σ0(a) = ROTR(2,a) XOR ROTR(13,a) XOR ROTR(22,a)
 *
 * ROTR(n) is implemented as ShiftBlock(source, n) in the MSB-first bit ordering
 * used by this engine (positive shift distance rotates toward higher indices = right).
 */
public class BigSigma0Block extends ExpressionBlock
{
    public BigSigma0Block(ExpressionBlock a) throws ExpressionBlockSizeError
    {
        super(a.size());

        ShiftBlock rotr2  = new ShiftBlock(a, 2);
        ShiftBlock rotr13 = new ShiftBlock(a, 13);
        ShiftBlock rotr22 = new ShiftBlock(a, 22);

        XorExpressionBlock xor1   = new XorExpressionBlock(rotr2, rotr13);
        XorExpressionBlock result = new XorExpressionBlock(xor1, rotr22);

        setExpressions(result);
    }
}
