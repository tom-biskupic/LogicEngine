package com.runcible.logicengine.structure;

import java.math.BigInteger;

import com.runcible.logicengine.logic.NotConstantError;

public class TerminalBlock extends ExpressionBlock
{
    public TerminalBlock( ExpressionBlock source )
    {
        super(source.size());
        
        try
        {
            setExpressions(source);
        }
        catch (ExpressionBlockSizeError e)
        {
            //
            //  Shouldn't happen
            //
            e.printStackTrace();
        }
    }
    
    public String valueAsBinary() throws NotConstantError
    {
        String result = "";
        
        for(int i=0;i<size();i++)
        {
            result += getExpression(i).getBooleanValue().getValue() == true ? "1" : "0";
        }
        
        return result;
    }
    
    public String valueAsHex() throws NotConstantError
    {
        return new BigInteger(valueAsBinary(),2).toString(16).toUpperCase();
    }
}
