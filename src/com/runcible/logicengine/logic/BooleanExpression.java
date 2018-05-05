package com.runcible.logicengine.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BooleanExpression
{
    public BooleanExpression(ExpressionType type,String name)
    {
        this.type = type;
        this.name=name;
        this.value=new BooleanValue();
        this.memento=null;
        this.subExpressions = new ArrayList<BooleanExpression>();
    }

    public BooleanExpression(ExpressionType type)
    {
        this(type,type.toString());
    }

    public BooleanExpression(ExpressionType type, BooleanExpression... subExpressions)
    {
        this(type,type.toString());
        this.subExpressions = Arrays.asList(subExpressions);
    }

    public void replaceWith(BooleanExpression newExpression)
    {
        this.type = newExpression.type;
        this.name = newExpression.name;
        this.value = newExpression.value;
        this.memento = newExpression.memento;
        this.subExpressions = newExpression.subExpressions;
    }
    
    public List<BooleanExpression> getSubExpressions()
    {
        return this.subExpressions;
    }

    public void setSubExpressions(List<BooleanExpression> expressions)
    {
        this.subExpressions = expressions;
    }

    public void addSubExpression(BooleanExpression expression)
    {
        this.subExpressions.add(expression);
    }

    public void clearSubExpressions()
    {
        subExpressions = new ArrayList<BooleanExpression>();
    }

    public ExpressionType getType()
    {
        return type;
    }

    public void setType(ExpressionType type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BooleanValue getBooleanValue()
    {
        return value;
    }

    public void setBooleanValue(BooleanValue value)
    {
        this.value = value;
    }

    public void apply(BooleanExpressionVisitor visitor)
    {
        visitor.visit(this);
        
        clearMementos();
    }
    
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
    
    private List<BooleanExpression> subExpressions;
    
    private ExpressionType          type;
    private String                  name;
    private BooleanValue            value;
    private Memento                 memento;
}
