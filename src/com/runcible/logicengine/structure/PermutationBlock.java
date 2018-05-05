package com.runcible.logicengine.structure;

public class PermutationBlock extends ExpressionBlock
{

    public PermutationBlock( ExpressionBlock source, int outputs, int[] bitOrder)
    {
        super(outputs);
        
        for(int i=0;i<outputs;i++)
        {
            //
            //  The bit-order data is 1 based - must make it zero based
            //
            setExpression(i,source.getExpression(bitOrder[i]-1));
        }
    }
}
