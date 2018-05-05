package com.runcible.logicengine.des.permutations;

import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.PermutationBlock;

public class ExpansionPermutation extends PermutationBlock
{

    public ExpansionPermutation(ExpressionBlock source)
    {
        super(source, 48, bitOrder);
    }

    public final static int[] bitOrder = 
    {
        32,  1,  2,  3,  4,  5,
         4,  5,  6,  7,  8,  9,
         8,  9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32,  1
    };
            
}
