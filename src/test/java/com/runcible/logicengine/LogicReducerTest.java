package com.runcible.logicengine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.ConstantExpression;
import com.runcible.logicengine.logic.ExpressionType;
import com.runcible.logicengine.logic.LogicReducer;
import com.runcible.logicengine.logic.NotConstantError;

public class LogicReducerTest
{

    @Test
    public void testNotWithConstant() throws NotConstantError
    {
        BooleanExpression notConst = BooleanExpression.makeNot(new ConstantExpression(false));
        
        notConst.apply(new LogicReducer());
        
        assertTrue(notConst.getBooleanValue().hasValue());
        assertEquals(true,notConst.getBooleanValue().getValue());
    }
    
    @Test
    public void testNotNot() throws NotConstantError
    {
        BooleanExpression subExpr = BooleanExpression.makeAnd(
                new ConstantExpression("A"),
                new ConstantExpression("B"));
        
        BooleanExpression notNot = BooleanExpression.makeNot(
                BooleanExpression.makeNot(subExpr));

        notNot.apply(new LogicReducer());
        
        assertEquals(subExpr.getType(),notNot.getType());
        assertEquals(subExpr.getSubExpressions(),notNot.getSubExpressions());
    }
    
    @Test
    public void testAndShortCircuit() throws NotConstantError
    {
        BooleanExpression and = BooleanExpression.makeAnd(
                new ConstantExpression("X"),
                new ConstantExpression(false));
        
        and.apply(new LogicReducer());
        
        assertTrue(and.getBooleanValue().hasValue());
        assertEquals(false,and.getBooleanValue().getValue());
    }
    
    @Test
    public void testAndIgnoreTrue() throws NotConstantError
    {
        BooleanExpression and = BooleanExpression.makeAnd(
                new ConstantExpression("X"),
                new ConstantExpression(true));
        
        and.apply(new LogicReducer());
        
        //
        //  So (X and True) should reduce to just X
        //
        assertFalse(and.getBooleanValue().hasValue());
        assertEquals(ExpressionType.TERM,and.getType());
        assertEquals("X",and.getName());
    }

    
    @Test
    public void testAndWithSameSubExprTwice() throws NotConstantError
    {
        BooleanExpression xTerm = new ConstantExpression("X");
        BooleanExpression and = BooleanExpression.makeAnd(
                new ConstantExpression("Y"),
                xTerm,
                xTerm);
        
        and.apply(new LogicReducer());
        
        assertEquals(2,and.getSubExpressions().size());
    }
    
    @Test
    public void testNotAndBecomesOr()
    {
        BooleanExpression a = new ConstantExpression("A");
        BooleanExpression b = new ConstantExpression("B");
        
        BooleanExpression testExpr = BooleanExpression.makeNot(BooleanExpression.makeAnd(a,b));
        
        testExpr.apply(new LogicReducer());
        
        assertEquals(ExpressionType.OR,testExpr.getType());
        assertEquals(ExpressionType.NOT,testExpr.getSubExpressions().get(0).getType());
        assertEquals(ExpressionType.NOT,testExpr.getSubExpressions().get(1).getType());
    }
    
    @Test
    public void testNotOrBecomesAnd()
    {
        BooleanExpression a = new ConstantExpression("A");
        BooleanExpression b = new ConstantExpression("B");
        
        BooleanExpression testExpr = BooleanExpression.makeNot(BooleanExpression.makeOr(a,b));
        
        testExpr.apply(new LogicReducer());
        
        assertEquals(ExpressionType.AND,testExpr.getType());
        assertEquals(ExpressionType.NOT,testExpr.getSubExpressions().get(0).getType());
        assertEquals(ExpressionType.NOT,testExpr.getSubExpressions().get(1).getType());
    }

    @Test
    public void testMergeSubOr()
    {
        BooleanExpression a = new ConstantExpression("A");
        BooleanExpression b = new ConstantExpression("B");
        BooleanExpression c = new ConstantExpression("c");
        
        BooleanExpression testExpr = BooleanExpression.makeOr(a,BooleanExpression.makeOr(b,c));
        testExpr.apply(new LogicReducer());
        assertEquals(3,testExpr.getSubExpressions().size());
        assertEquals(ExpressionType.OR,testExpr.getType());
        assertTrue(testExpr.getSubExpressions().contains(a));
        assertTrue(testExpr.getSubExpressions().contains(b));
        assertTrue(testExpr.getSubExpressions().contains(c));
    }
    
    @Test
    public void testMergeSubAnd()
    {
        BooleanExpression a = new ConstantExpression("A");
        BooleanExpression b = new ConstantExpression("B");
        BooleanExpression c = new ConstantExpression("c");
        
        BooleanExpression testExpr = BooleanExpression.makeAnd(a,BooleanExpression.makeAnd(b,c));
        testExpr.apply(new LogicReducer());
        assertEquals(3,testExpr.getSubExpressions().size());
        assertEquals(ExpressionType.AND,testExpr.getType());
        assertTrue(testExpr.getSubExpressions().contains(a));
        assertTrue(testExpr.getSubExpressions().contains(b));
        assertTrue(testExpr.getSubExpressions().contains(c));
    }

    @Test
    public void testMergDemorgans()
    {
        BooleanExpression a = new ConstantExpression("A");
        BooleanExpression b = new ConstantExpression("B");
        BooleanExpression c = new ConstantExpression("c");
        
        BooleanExpression testExpr = BooleanExpression.makeOr(a,BooleanExpression.makeNot(BooleanExpression.makeAnd(b,c)));
        
        testExpr.apply(new LogicReducer());
        assertEquals(3,testExpr.getSubExpressions().size());
        assertEquals(ExpressionType.OR,testExpr.getType());
        assertTrue(testExpr.getSubExpressions().contains(a));
        
        assertEquals(ExpressionType.NOT,testExpr.getSubExpressions().get(1).getType());
        assertEquals(b,testExpr.getSubExpressions().get(1).getSubExpressions().get(0));
        assertEquals(c,testExpr.getSubExpressions().get(0).getSubExpressions().get(0));
    }
}
