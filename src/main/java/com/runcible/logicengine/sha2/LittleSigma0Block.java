package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.ShrBlock;
import com.runcible.logicengine.structure.ShiftBlock;
import com.runcible.logicengine.structure.XorExpressionBlock;

/**
 * SHA-256 lower-case sigma-0 (message schedule):
 *   σ0(w) = ROTR(7,w) XOR ROTR(18,w) XOR SHR(3,w)
 */
public class LittleSigma0Block extends ExpressionBlock
{
    public LittleSigma0Block(ExpressionBlock w) throws ExpressionBlockSizeError
    {
        super(w.size());

        ShiftBlock rotr7  = new ShiftBlock(w, 7);
        ShiftBlock rotr18 = new ShiftBlock(w, 18);
        ShrBlock   shr3   = new ShrBlock(w, 3);

        XorExpressionBlock xor1   = new XorExpressionBlock(rotr7, rotr18);
        XorExpressionBlock result = new XorExpressionBlock(xor1, shr3);

        setExpressions(result);
    }
}
