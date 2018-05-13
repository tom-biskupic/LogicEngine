package com.runcible.logicengine.des;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.runcible.logicengine.logic.Evaluator;
import com.runcible.logicengine.logic.LogicReducer;
import com.runcible.logicengine.logic.NotConstantError;
import com.runcible.logicengine.logic.Printer;
import com.runcible.logicengine.structure.ConstantExpressionBlock;
import com.runcible.logicengine.structure.ExpressionBlockSizeError;
import com.runcible.logicengine.structure.TerminalBlock;

public class DESTest
{
    @Test
    public void testDES() throws ExpressionBlockSizeError, NotConstantError
    {
        ConstantExpressionBlock plainText = new ConstantExpressionBlock(64,"p");
        plainText.setValueFromHex("382062797465733A");
        
        ConstantExpressionBlock key = new ConstantExpressionBlock(64,"k");
        key.setValueFromHex("1C07F4D66B4338F1");
        
        DES desCipher = new DES(plainText,key);
        
        TerminalBlock terminal = new TerminalBlock(desCipher);
        
        desCipher.apply(new Evaluator());
        
        assertEquals("85B4CC2513DED94A",terminal.valueAsHex());
    }
    
    @Test
    public void testReduce() throws ExpressionBlockSizeError, NotConstantError
    {
        ConstantExpressionBlock plainText = new ConstantExpressionBlock(64,"p");
        plainText.setValueFromHex("382062797465733A");
        
        ConstantExpressionBlock key = new ConstantExpressionBlock(64,"k");

        DES desCipher = new DES(plainText,key);
        
        TerminalBlock terminal = new TerminalBlock(desCipher);

        System.out.println("Running Evaluator...");
        Evaluator evaluator = new Evaluator();
        desCipher.apply(evaluator);
        System.out.println(evaluator.statsAsString());
        
        System.out.println("Running Reducer...");
        LogicReducer reducer = new LogicReducer();
        desCipher.apply(reducer);
  
        System.out.println(reducer.statsAsString());
        desCipher.getExpression(0).apply(new Printer(2000));

        //
        //  Check the result is still correct after reduction
        //
        key.setValueFromHex("1C07F4D66B4338F1");
        desCipher.apply(evaluator);
        assertEquals("85B4CC2513DED94A",terminal.valueAsHex());
    }
}
