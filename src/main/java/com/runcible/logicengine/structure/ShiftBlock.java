package com.runcible.logicengine.structure;

public class ShiftBlock extends ExpressionBlock
{

    public ShiftBlock( ExpressionBlock source,int shiftDistance)
    {
        super(source.size());
        
        for(int i=0;i<size();i++)
        {
            int targetLoc = i + shiftDistance;

            if ( targetLoc < 0 )
            {
                targetLoc += size();
            }

            targetLoc = targetLoc % size();

            setExpression(targetLoc,source.getExpression(i) );
        }
    }
}
