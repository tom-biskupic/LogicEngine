package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.ShiftBlock;
import com.runcible.logicengine.structure.XorExpressionBlock;

/**
 * SHA-256 upper-case Sigma-1: Σ1(e) = ROTR(6,e) XOR ROTR(11,e) XOR ROTR(25,e)
 */
public class BigSigma1Block extends ExpressionBlock
{
    public BigSigma1Block(ExpressionBlock e) throws ExpressionBlockSizeError
    {
        super(e.size());

        ShiftBlock rotr6  = new ShiftBlock(e, 6);
        ShiftBlock rotr11 = new ShiftBlock(e, 11);
        ShiftBlock rotr25 = new ShiftBlock(e, 25);

        XorExpressionBlock xor1   = new XorExpressionBlock(rotr6, rotr11);
        XorExpressionBlock result = new XorExpressionBlock(xor1, rotr25);

        setExpressions(result);
    }
}
