package com.runcible.logicengine.structure;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.ExpressionType;

public class XorExpressionBlock extends ExpressionBlock
{

    public XorExpressionBlock(ExpressionBlock leftSource, ExpressionBlock rightSource) throws ExpressionBlockSizeError
    {
        super(leftSource.size());
        
        if ( leftSource.size() != rightSource.size() )
        {
            throw new ExpressionBlockSizeError("Left source = "+leftSource+"Right source = "+rightSource.size());
        }
        
        for(int i=0;i<size();i++)
        {
            BooleanExpression left = leftSource.getExpression(i);
            BooleanExpression right = rightSource.getExpression(i);
            
            setExpression(i,makeXOR(left, right));
        }
    }

    private BooleanExpression makeXOR(
            BooleanExpression left,
            BooleanExpression right)
    {
//        //
//        //  We implement XOR as (a|b) & !(a&b)
//        //
//        return new BooleanExpression(
//            ExpressionType.AND,
//            new BooleanExpression(ExpressionType.OR,left,right),
//            new BooleanExpression(ExpressionType.NOT,
//                    new BooleanExpression(ExpressionType.AND,left,right)));
        
        //
        //  We implement XOR as (a|b) & (!a|!b)
        //
        return new BooleanExpression(
            ExpressionType.AND,
            new BooleanExpression(ExpressionType.OR,left,right),
            new BooleanExpression(ExpressionType.OR,
                    new BooleanExpression(ExpressionType.NOT,left),
                    new BooleanExpression(ExpressionType.NOT,right)));

    }
}
