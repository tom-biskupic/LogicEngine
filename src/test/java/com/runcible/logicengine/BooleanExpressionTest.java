package com.runcible.logicengine;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.ExpressionType;


public class BooleanExpressionTest
{
    private BooleanExpression subTerm1 = new BooleanExpression(ExpressionType.TERM,"k1");
    private BooleanExpression subTerm2 = new BooleanExpression(ExpressionType.TERM,"k2");

    @Test
    public void testConstruction()
    {
        Set<BooleanExpression> exprs = new HashSet<BooleanExpression>();
        
        checkValue(new BooleanExpression(ExpressionType.AND), ExpressionType.AND.toString(), ExpressionType.AND,exprs);
        checkValue(new BooleanExpression(ExpressionType.TERM,"fred"),"fred", ExpressionType.TERM,exprs);
        
        exprs.add(subTerm1);
        exprs.add(subTerm2);
        checkValue(new BooleanExpression(ExpressionType.AND,subTerm1,subTerm2),ExpressionType.AND.toString(),ExpressionType.AND,exprs);
    }

    @Test public void testReplaceWith()
    {
        BooleanExpression toBeReplaced = new BooleanExpression(ExpressionType.AND,subTerm1,subTerm2);
        
        BooleanExpression replaceWith = new BooleanExpression(ExpressionType.TERM);
    }
    
    private void checkValue(
            BooleanExpression       newExpression, 
            String                  name, 
            ExpressionType          expressionType,
            Set<BooleanExpression>  subExprs)
    {
        assertEquals(expressionType,newExpression.getType());
        assertEquals(subExprs.size(),newExpression.getSubExpressions().size());
        for(BooleanExpression b : newExpression.getSubExpressions() )
        {
            assertTrue(subExprs.contains(b));
        }
        assertEquals(name,newExpression.getName());
        assertEquals(null,newExpression.getMemento());
        assertEquals(false,newExpression.getBooleanValue().hasValue());
    }
}
