package com.runcible.logicengine.des;

import com.runcible.logicengine.des.permutations.ExpansionPermutation;
import com.runcible.logicengine.des.permutations.PBoxPermutation;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.TerminalBlock;
import com.runcible.logicengine.structure.XorExpressionBlock;

public class Round extends ExpressionBlock
{

    public Round(ExpressionBlock input, ExpressionBlock subKey) throws ExpressionBlockSizeError
    {
        super(64);
        
        ExpressionBlock leftInputHalf = input.getSlice(0, 32);
        ExpressionBlock rightInputHalf = input.getSlice(32, 32);
        
        //
        //  Set the left output bits equal to the right inputs
        //  passed from above.
        //
        setExpressions(rightInputHalf);

        ExpansionPermutation expansionPermutation = new ExpansionPermutation(rightInputHalf);
        
        //
        //  XOR the result of the Expansion permutation with the key
        //
        XorExpressionBlock xorBlock1 = new XorExpressionBlock(expansionPermutation, subKey);
        
        SubstitutionBoxBlock  sboxBlock = new SubstitutionBoxBlock(xorBlock1);
        
        PBoxPermutation pbox = new PBoxPermutation(sboxBlock);
        
        //
        //  Now the second XOR block takes the left hand half of the input
        //  and the output of the PBox
        //
        XorExpressionBlock xorBlock2 = new XorExpressionBlock(leftInputHalf, pbox);
        
        //
        //  The XorBlock contains the right-hand side output expressions
        //  copy these now.
        //
        setExpressions(xorBlock2,32);
    }
}
