package com.runcible.logicengine.logic;

/**
 * A Boolean value holds a constant value that is injected into a TERM logic expression.
 * 
 * @author tom
 *
 */
public class BooleanValue
{
    public BooleanValue()
    {
    }

    public BooleanValue(boolean value)
    {
        setValue(value);
    }
    
    public boolean getValue() throws NotConstantError
    {
        if ( !hasValue )
        {
            throw new NotConstantError();
        }

        return value;
    }

    public void setValue(boolean value)
    {
        this.hasValue = true;
        this.value = value;
    }

    public boolean hasValue()
    {
        return hasValue;
    }

    private boolean hasValue = false;
    private boolean value = false;
}
