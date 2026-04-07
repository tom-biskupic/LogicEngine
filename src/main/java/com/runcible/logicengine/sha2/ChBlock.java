package com.runcible.logicengine.sha2;

import com.runcible.logicengine.structure.AndExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.NotExpressionBlock;
import com.runcible.logicengine.structure.XorExpressionBlock;

/**
 * SHA-256 Choice function: Ch(e,f,g) = (e AND f) XOR (NOT(e) AND g)
 */
public class ChBlock extends ExpressionBlock
{
    public ChBlock(ExpressionBlock e, ExpressionBlock f, ExpressionBlock g)
        throws ExpressionBlockSizeError
    {
        super(e.size());

        NotExpressionBlock notE    = new NotExpressionBlock(e);
        AndExpressionBlock eAndF   = new AndExpressionBlock(e, f);
        AndExpressionBlock notEAndG = new AndExpressionBlock(notE, g);
        XorExpressionBlock result  = new XorExpressionBlock(eAndF, notEAndG);

        setExpressions(result);
    }
}
