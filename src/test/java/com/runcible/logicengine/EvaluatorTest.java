package com.runcible.logicengine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.ConstantExpression;
import com.runcible.logicengine.logic.Evaluator;
import com.runcible.logicengine.logic.ExpressionType;
import com.runcible.logicengine.logic.NotConstantError;

public class EvaluatorTest 
{

    @Test
    public void testNot() throws NotConstantError
    {
        BooleanExpression notExpr = BooleanExpression.makeNot(new ConstantExpression(false));
        
        Evaluator evaluator = new Evaluator();
        
        notExpr.apply(evaluator);
        
        assertEquals(true,notExpr.getBooleanValue().getValue());
    }
    
    @Test(expected=NotConstantError.class)
    public void testNotNotConstant() throws NotConstantError
    {
        BooleanExpression notExpr = BooleanExpression.makeNot(new ConstantExpression("X"));
        
        Evaluator evaluator = new Evaluator();
        
        notExpr.apply(evaluator);
        
        notExpr.getBooleanValue().getValue();
    }

    @Test
    public void testNotAlreadyEvaluated() throws NotConstantError
    {
        BooleanExpression notExpr = BooleanExpression.makeNot(new ConstantExpression(false));
        
        Evaluator evaluator = new Evaluator();
        
        notExpr.apply(evaluator);
        notExpr.apply(evaluator);
        
        assertEquals(true,notExpr.getBooleanValue().getValue());
    }

    @Test
    public void testOrLogic() throws NotConstantError
    {
        testExpr(ExpressionType.OR,false,false, false);
        testExpr(ExpressionType.OR,false,true, true);
        testExpr(ExpressionType.OR,true,false, true);
        testExpr(ExpressionType.OR,true,true, true);
    }
    
    @Test 
    public void testOrShortCut() throws NotConstantError
    {
        //
        //  So one expression is true but the other is non-const. In this case
        //  we evaluate to true anyway.
        //
        BooleanExpression orExpr = BooleanExpression.makeOr(
                new ConstantExpression(true),
                new ConstantExpression("X"));
        
        orExpr.apply(new Evaluator());
        assertEquals(true,orExpr.getBooleanValue().getValue());
    }

    @Test 
    public void testOrNotConst() throws NotConstantError
    {
        //
        //  One expression is false and the other is non-const. In this
        //  case the result is non-const
        //
        BooleanExpression orExpr = BooleanExpression.makeOr(
                new ConstantExpression(false),
                new ConstantExpression("X"));
        
        orExpr.apply(new Evaluator());
        assertEquals(false,orExpr.getBooleanValue().hasValue());
    }

    @Test
    public void testAndLogic() throws NotConstantError
    {
        testExpr(ExpressionType.AND,false,false, false);
        testExpr(ExpressionType.AND,false,true, false);
        testExpr(ExpressionType.AND,true,false, false);
        testExpr(ExpressionType.AND,true,true, true);
    }

    @Test 
    public void testAndShortCut() throws NotConstantError
    {
        //
        //  So one expression is false but the other is non-const. 
        //  In this case the result depends on the the non-const expression
        //  so the result is non-const
        //
        BooleanExpression andExpr = BooleanExpression.makeAnd(
                new ConstantExpression(false),
                new ConstantExpression("X"));
        
        andExpr.apply(new Evaluator());
        assertEquals(false,andExpr.getBooleanValue().getValue());
    }

    @Test 
    public void testAndNotConst() throws NotConstantError
    {
        //
        //  So one expression is true but the other is non-const. In this case
        //  we evaluate to false anyway.
        //
        BooleanExpression andExpr = BooleanExpression.makeAnd(
                new ConstantExpression(true),
                new ConstantExpression("X"));
        
        andExpr.apply(new Evaluator());
        assertEquals(false,andExpr.getBooleanValue().hasValue());
    }

    @Test
    public void testSubExpr() throws NotConstantError
    {
        BooleanExpression expr = new BooleanExpression(
                ExpressionType.AND,
                new BooleanExpression(
                        ExpressionType.OR,
                        new ConstantExpression(false),
                        new ConstantExpression(true)),
                new ConstantExpression(true));
        expr.apply(new Evaluator());
        
        assertEquals(true,expr.getBooleanValue().getValue());
    }
    
    private void testExpr(ExpressionType expressionType,boolean value1,boolean value2, boolean expected) throws NotConstantError
    {
        BooleanExpression expr = new BooleanExpression(
                expressionType,
                new ConstantExpression(value1),
                new ConstantExpression(value2));

        expr.apply(new Evaluator());
        assertEquals(expected,expr.getBooleanValue().getValue());
    }
}
