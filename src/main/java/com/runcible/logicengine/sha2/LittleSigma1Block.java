package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.ShrBlock;
import com.runcible.logicengine.structure.ShiftBlock;
import com.runcible.logicengine.structure.XorExpressionBlock;

/**
 * SHA-256 lower-case sigma-1 (message schedule):
 *   σ1(w) = ROTR(17,w) XOR ROTR(19,w) XOR SHR(10,w)
 */
public class LittleSigma1Block extends ExpressionBlock
{
    public LittleSigma1Block(ExpressionBlock w) throws ExpressionBlockSizeError
    {
        super(w.size());

        ShiftBlock rotr17 = new ShiftBlock(w, 17);
        ShiftBlock rotr19 = new ShiftBlock(w, 19);
        ShrBlock   shr10  = new ShrBlock(w, 10);

        XorExpressionBlock xor1   = new XorExpressionBlock(rotr17, rotr19);
        XorExpressionBlock result = new XorExpressionBlock(xor1, shr10);

        setExpressions(result);
    }
}
