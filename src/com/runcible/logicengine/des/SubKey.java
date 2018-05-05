package com.runcible.logicengine.des;

import com.runcible.logicengine.des.permutations.CompressionPermutation;
import com.runcible.logicengine.des.permutations.InitialKeyPermutation;
import com.runcible.logicengine.structure.ExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.ShiftBlock;

public class SubKey extends ExpressionBlock
{
    public SubKey( ExpressionBlock key, int round) throws ExpressionBlockSizeError
    {
        super(48);
        
        InitialKeyPermutation initialKeyPermutation = new InitialKeyPermutation(key);
        
        ShiftBlock leftShift = new ShiftBlock(initialKeyPermutation.getSlice(0, 28),0-totalRotations[round]);
        ShiftBlock rightShift = new ShiftBlock(initialKeyPermutation.getSlice(28,28),0-totalRotations[round]);
        
        ExpressionBlock cpInput = new ExpressionBlock(56);
        cpInput.setExpressions(leftShift);
        cpInput.setExpressions(rightShift,28);
        
        CompressionPermutation compressionPermutation = new CompressionPermutation(cpInput);
        
        setExpressions(compressionPermutation);
    }
    
    //
    //  This is an array of constants that correspond to the total shift distance
    //  per round of the des key. This is used for calculating sub-keys.
    //
    private static final int[] totalRotations = 
    {
       1,2,4,6,8,10,12,14,15,17,19,21,23,25,27,28
    };

}
