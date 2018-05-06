package com.runcible.logicengine.logic;

/**
 * Convenient sub-class of a BooleanExpression that can be used to make
 * constant values
 */
public class ConstantExpression extends BooleanExpression
{
    /**
     * Create a constant with the name given
     * @param name
     */
    public ConstantExpression(String name)
    {
        super(ExpressionType.TERM,name);
    }
    
    /**
     * Create a constant with the name given and the value specified
     * @param name
     */
    public ConstantExpression(boolean value)
    {
        super(ExpressionType.TERM);
        
        getBooleanValue().setValue(value);
    }
    
}
