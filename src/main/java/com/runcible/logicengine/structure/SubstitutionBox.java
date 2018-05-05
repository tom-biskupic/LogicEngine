package com.runcible.logicengine.structure;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.ExpressionType;

public class SubstitutionBox extends ExpressionBlock
{
    public SubstitutionBox(ExpressionBlock source, int size, int[] sboxData)
    {
        super(size);
        
        for(int i=0;i<size();i++)
        {
            setExpression(i,buildExpression(source,i,size,sboxData));
        }
    }

    private BooleanExpression buildExpression(
            ExpressionBlock input, 
            int             n,
            int             size, 
            int[]           sboxData)
    {
        BooleanExpression   resultExpr;
        long                mask = ( 1 << (size-n-1) );
        BooleanExpression   subAndExpr = null;
        BooleanExpression   not = null;

        //
        //  Now we iterate over each entry in the source data and check the
        //  bit corresponding to the output bit we are building an expression for
        //
        //  If the bit is set then we build an expression for the input bits
        //  corresponding to that item of source data
        //
        resultExpr = new BooleanExpression(ExpressionType.OR);

        for(int i=0;i<(1 << input.size());i++)
        {
            if ( (sboxData[i] & mask) != 0 )
            {
                subAndExpr = new BooleanExpression(ExpressionType.AND);

                int inputBitMask = (1 << (input.size()-1));

                for(int k=0;k<input.size();k++)
                {
                    if ( (i & inputBitMask) != 0 )
                    {
                        subAndExpr.addSubExpression(input.getExpression(k));
                    } 
                    else
                    {
                        not = new BooleanExpression(ExpressionType.NOT);

                        not.addSubExpression(input.getExpression(k));
                        subAndExpr.addSubExpression(not);
                    }

                    inputBitMask = inputBitMask >> 1;
                }

                resultExpr.addSubExpression(subAndExpr);
            }
        }

        return resultExpr;
    }
}
