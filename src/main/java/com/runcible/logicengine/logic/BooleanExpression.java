package com.runcible.logicengine.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BooleanExpression
{
	/**
	 * Constructs a Boolean expression of the type specified with the 
	 * name specified. The name is used for printing.
	 * @param type The type of the expression
	 * @param name The name of the exression
	 */
    public BooleanExpression(ExpressionType type,String name)
    {
        this.type = type;
        this.name=name;
        this.value=new BooleanValue();
        this.memento=null;
        this.subExpressions = new ArrayList<BooleanExpression>();
    }

    /**
     * Constructs a Boolean expression of the type specified.
     * @param type The type to create
     */
    public BooleanExpression(ExpressionType type)
    {
        this(type,type.toString());
    }

    /**
     * Create a Boolean expression with initialized with a set of sub
     * expressions. For example if this is an OR expression, the sub-expressions
     * would be parameters to the OR.
     * @param type The type of this expression
     * @param subExpressions a list of sub-expressions to add
     */
    public BooleanExpression(ExpressionType type, BooleanExpression... subExpressions)
    {
        this(type,type.toString());
        this.subExpressions = Arrays.asList(subExpressions);
    }

    /**
     * Replaces the Boolean expression with an alternative one. Used to replace
     * an expression 'in-place' without messing up the references to the existing
     * expression
     * @param newExpression The expression to copy into this one.
     */
    public void replaceWith(BooleanExpression newExpression)
    {
        this.type = newExpression.type;
        this.name = newExpression.name;
        this.value = newExpression.value;
        this.memento = newExpression.memento;
        this.subExpressions = newExpression.subExpressions;
    }
    
    /**
     * Returns the list of sub-expressions to this expression
     * @return the list of sub-expressions
     */
    public List<BooleanExpression> getSubExpressions()
    {
        return this.subExpressions;
    }

    /**
     * Replaces the set of sub-expressions with the list provided 
     * @param expressions The list of sub-expressions to replace the current ones
     */
    public void setSubExpressions(List<BooleanExpression> expressions)
    {
        this.subExpressions = expressions;
    }

    /**
     * Adds a sub-expression to the expression
     * @param expression The sub-expression to add
     */
    public void addSubExpression(BooleanExpression expression)
    {
        this.subExpressions.add(expression);
    }

    /**
     * Removes all the sub-expressions to this expression
     */
    public void clearSubExpressions()
    {
        subExpressions = new ArrayList<BooleanExpression>();
    }

    /**
     * Returns the type of this Boolean expression
     * @return The type
     */
    public ExpressionType getType()
    {
        return type;
    }

    /**
     * Sets the type of this Boolean expression
     * @param type the type
     */
    public void setType(ExpressionType type)
    {
        this.type = type;
    }

    /**
     * Returns the name of this expression
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this expression
     * @param name The new name to apply
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the boolean value for this expression
     * @return The boolean value
     */
    public BooleanValue getBooleanValue()
    {
        return value;
    }

    /**
     * Sets the boolean value of this expression. Used
     * when evaluating the expression
     * @param value The new boolean value
     */
    public void setBooleanValue(BooleanValue value)
    {
        this.value = value;
    }

    /**
     * Applys the visitor to this boolean expression
     * @param visitor The visitor to apply
     */
    public void apply(BooleanExpressionVisitor visitor)
    {
        visitor.visit(this);
        //applyInternal(visitor);
        clearMementos();
    }

    public void applyInternal(BooleanExpressionVisitor visitor)
    {
        for(BooleanExpression b : subExpressions )
        {
            b.applyInternal(visitor);
        }
        
        visitor.visit(this);
    }

    /**
     * Clears any mementos stored by visitors in this
     * Boolean expression
     */
    private void clearMementos()
    {
        if ( this.memento != null )
        {
            for( BooleanExpression b : subExpressions )
            {
                b.clearMementos();
            }
            
            this.memento = null;
        }
    }

    /**
     * Stores a memento for later retrieval. Used by the visitor
     * to store visitor specific info.
     * @return
     */
    public Memento getMemento()
    {
        return this.memento;
    }

    /**
     * Retrieves the memento stored previously. Used by the visitor
     * to store visitor specific info.
     * @param memento The memento to store
     */
    public void setMemento( Memento memento )
    {
        this.memento = memento;
    }
    
    public static BooleanExpression makeAnd(BooleanExpression... subExpressions)
    {
        return new BooleanExpression(ExpressionType.AND,subExpressions);
    }

    public static BooleanExpression makeOr(BooleanExpression... subExpressions)
    {
        return new BooleanExpression(ExpressionType.OR,subExpressions);
    }

    public static BooleanExpression makeNot(BooleanExpression subExpression)
    {
        return new BooleanExpression(ExpressionType.NOT,subExpression);
    }

    private List<BooleanExpression> subExpressions;
    
    private ExpressionType          type;
    private String                  name;
    private BooleanValue            value;
    private Memento                 memento;
}
