package com.runcible.logicengine.des;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.Evaluator;
import com.runcible.logicengine.logic.ExpressionType;
import com.runcible.logicengine.logic.LogicReducer;
import com.runcible.logicengine.logic.NotConstantError;
import com.runcible.logicengine.logic.Printer;
import com.runcible.logicengine.structure.ConstantExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.TerminalBlock;

public class DESTest
{
//    @Test
//    public void testDuplicates()
//    {
//        BooleanExpression terminal = new BooleanExpression(ExpressionType.TERM,"k");
//        
//        BooleanExpression andWithDups = new BooleanExpression(ExpressionType.AND,terminal,terminal);
//        
//        LogicReducer reducer = new LogicReducer();
//        andWithDups.apply(reducer);
//        System.out.println(reducer.statsAsString());
//    }
    
//  @Test
//  public void testMergeDemorgans()
//  {
//      BooleanExpression terminala = new BooleanExpression(ExpressionType.TERM,"a");
//      BooleanExpression terminalb = new BooleanExpression(ExpressionType.TERM,"b");
//      BooleanExpression terminalc = new BooleanExpression(ExpressionType.TERM,"c");
//      
//      BooleanExpression andWithNotOr = new BooleanExpression(
//                                          ExpressionType.AND,terminala,
//                                          new BooleanExpression(ExpressionType.NOT,
//                                                  new BooleanExpression(ExpressionType.OR,terminalb,terminalc)));
//      
//      LogicReducer reducer = new LogicReducer();
//      andWithNotOr.apply(reducer);
//      System.out.println(reducer.statsAsString());
//      andWithNotOr.apply(new Printer());
//  }

    @Test
    public void testDES() throws ExpressionBlockSizeError, NotConstantError
    {
        ConstantExpressionBlock plainText = new ConstantExpressionBlock(64,"p");
        plainText.setValueFromHex("382062797465733A");
        
        ConstantExpressionBlock key = new ConstantExpressionBlock(64,"k");
        //key.setValueFromHex("1C07F4D66B4338F1");
        
        DES desCipher = new DES(plainText,key);
        
        TerminalBlock terminal = new TerminalBlock(desCipher);
        
        desCipher.apply(new Evaluator());
        
        LogicReducer reducer = new LogicReducer();
        desCipher.apply(reducer);
        
        System.out.println(reducer.statsAsString());
        
        desCipher.getExpression(0).apply(new Printer(2000));
        
        key.setValueFromHex("1C07F4D66B4338F1");
        
        reducer.reset();
        
        desCipher.apply(reducer);
//
        System.out.println(reducer.statsAsString());
//
        desCipher.getExpression(0).apply(new Printer());
        
        assertEquals("85B4CC2513DED94A",terminal.valueAsHex());
        
        //// XXXX sdfsdfsdf
        
        
    }
}
