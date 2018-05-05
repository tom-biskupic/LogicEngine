package com.runcible.logicengine.des;

import com.runcible.logicengine.des.permutations.FinalPermutation;
import com.runcible.logicengine.des.permutations.InitialPermutation;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;

public class DES extends ExpressionBlock
{
    public DES( ExpressionBlock input, ExpressionBlock key) throws ExpressionBlockSizeError
    {
        super(64);

        //
        //  First declare the sub-keys
        //
        SubKey[] subKeys = new SubKey[16];
        
        for(int i=0;i<16;i++)
        {
            subKeys[i] = new SubKey(key,i);
        }

        InitialPermutation initialPermutation = new InitialPermutation(input);
        
        Round[] rounds = new Round[16];
        
        rounds[0] = new Round(initialPermutation,subKeys[0]);
        
        for(int i=1;i<16;i++)
        {
            rounds[i] = new Round(rounds[i-1],subKeys[i]);
        }
        
        ExpressionBlock stuff = new ExpressionBlock(64);
        stuff.setExpressions(rounds[15].getSlice(32,32));
        stuff.setExpressions(rounds[15].getSlice(0,32),32);

        FinalPermutation    finalPermutation = new FinalPermutation(stuff);

        setExpressions(finalPermutation);
    }
}
