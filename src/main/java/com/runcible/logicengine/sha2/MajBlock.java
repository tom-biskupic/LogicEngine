package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.AndExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.XorExpressionBlock;

/**
 * SHA-256 Majority function: Maj(a,b,c) = (a AND b) XOR (a AND c) XOR (b AND c)
 */
public class MajBlock extends ExpressionBlock
{
    public MajBlock(ExpressionBlock a, ExpressionBlock b, ExpressionBlock c)
        throws ExpressionBlockSizeError
    {
        super(a.size());

        AndExpressionBlock aAndB = new AndExpressionBlock(a, b);
        AndExpressionBlock aAndC = new AndExpressionBlock(a, c);
        AndExpressionBlock bAndC = new AndExpressionBlock(b, c);
        XorExpressionBlock xor1  = new XorExpressionBlock(aAndB, aAndC);
        XorExpressionBlock result = new XorExpressionBlock(xor1, bAndC);

        setExpressions(result);
    }
}
