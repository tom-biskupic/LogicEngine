package com.runcible.logicengine.logic;

/**
 * A Boolean value holds a constant value that is injected into a TERM logic expression or is 
 * set on an expression that evaluates to a constant (during evaluation).
 */
public class BooleanValue
{
	/**
	 * Construct an un-initialized boolean value.
	 */
    public BooleanValue()
    {
    }

    /**
     * Constructs a boolean value with the value specified
     * @param value The value of this boolean value
     */
    public BooleanValue(boolean value)
    {
        setValue(value);
    }
    
    /**
     * Copy constructor - constructs a boolean value with the same value as the other
     * @param other
     */
    public BooleanValue(BooleanValue other)
    {
        this.hasValue = other.hasValue;
        this.value = other.value;
    }

    @Override
    public boolean equals(Object other)
    {
        if ( other == null || !(other instanceof BooleanValue) )
        {
            return false;
        }

        BooleanValue otherBool = (BooleanValue)other;

        if ( this.hasValue != otherBool.hasValue )
        {
            return false;
        }

        if ( this.hasValue && this.value != otherBool.value )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() 
    {
        // Use the same fields as the equals method
        return java.util.Objects.hash(hasValue, value);
    }

    /**
     * Returns the value of this boolean value
     * @return The value of this boolean value
     * @throws NotConstantError if there is no value set (i.e. the expression is not constant)
     */
    public boolean getValue() throws NotConstantError
    {
        if ( !hasValue )
        {
            throw new NotConstantError();
        }

        return value;
    }

    /**
     * Sets the value of this boolean value.
     * @param value the value to assign
     */
    public void setValue(boolean value)
    {
        this.hasValue = true;
        this.value = value;
    }

    /**
     * Returns true if this boolean value has a value
     * @return
     */
    public boolean hasValue()
    {
        return hasValue;
    }

    /**
     * Marks this boolean value as having no value - i.e. is not
     * constant or not evaluated.
     * 
     */
    public void setHasNoValue()
    {
        hasValue = false;
    }
    
    private boolean hasValue = false;
    private boolean value = false;
}
