package com.runcible.logicengine.des.permutations;

import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.PermutationBlock;

public class PBoxPermutation extends PermutationBlock
{

    public PBoxPermutation(ExpressionBlock source)
    {
        super(source, 32, bitOrder);
    }

    private static final int[] bitOrder = 
    {
        16,  7, 20, 21,
        29, 12, 28, 17,
         1, 15, 23, 26,
         5, 18, 31, 10,
         2,  8, 24, 14,
        32, 27,  3,  9,
        19, 13, 30,  6,
        22, 11,  4, 25
    };
}
