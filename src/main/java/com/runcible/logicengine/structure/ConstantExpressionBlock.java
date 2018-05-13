package com.runcible.logicengine.structure;

import java.math.BigInteger;
import java.util.Vector;

import com.runcible.logicengine.logic.BooleanExpression;
import com.runcible.logicengine.logic.BooleanValue;
import com.runcible.logicengine.logic.ConstantExpression;
import com.runcible.logicengine.logic.ExpressionType;

/**
 * A constant expression block is used to create an input block that is either
 * an unknown value or a known value set from a binary string.
 *  
 * @author tom
 *
 */
public class ConstantExpressionBlock extends ExpressionBlock
{

    /**
     * Constructs a constant expression block.
     * @param size Size of the block
     * @param name the name of the block (used to label each expression)
     */
    public ConstantExpressionBlock(int size,String name)
    {
        super(size);
        
        values = new Vector<BooleanValue>(size,1);
        values.setSize(size);

        for(int i=0;i<size;i++)
        {
            BooleanExpression expression = new ConstantExpression(name+i);
            
            values.set(i, expression.getBooleanValue());
            
            setExpression(i, expression);
        }
    }

    /**
     * Sets the values of each bit in the block from the bits in string
     * @param binaryString
     * @throws ExpressionBlockSizeError 
     */
    public void setValueFromBinary(String binaryString) throws ExpressionBlockSizeError
    {
        if ( binaryString.length() < size() )
        {
            throw new ExpressionBlockSizeError("Value string not the same length as block");
        }
        
        for(int i=0;i<size();i++)
        {
            values.get(i).setValue(binaryString.charAt(i) == '1');
        }
    }
    
    public void setValueFromHex( String hexString ) throws ExpressionBlockSizeError
    {
        BigInteger asBigInt = new BigInteger(hexString, 16);
        String formatString = "%"+size()+"s";
        
        String asString = String.format(formatString,asBigInt.toString(2)).replace(" ", "0");
        setValueFromBinary(asString);
    }
    
    protected Vector<BooleanValue> values;
}
