package com.runcible.logicengine.logic;

public class Printer implements BooleanExpressionVisitor
{

    private int thisMany = 0;
    private int visited = 0;
    private boolean firstExpression = true;
    
    public Printer()
    {
    }

    public Printer(int thisMany )
    {
        this.thisMany = thisMany;
    }
    
    @Override
    public void visit(BooleanExpression expression)
    {
        visited++;
        
        if ( limitReached() )
        {
            return;
        }

        boolean thisIterIsFirst = firstExpression;
        firstExpression= false;
        
        switch(expression.getType())
        {
            case TERM:
            {
                if ( expression.getBooleanValue().hasValue() )
                {
                    try
                    {
                        System.out.print(expression.getBooleanValue().getValue());
                    }
                    catch (NotConstantError e)
                    {
                        //
                        //  Should not happen
                        //
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.out.print(expression.getName());
                }
                break;
            }
            case AND:
            {
                System.out.print("AND(");
                visitSubExpressions(expression);
                System.out.print(")");
                break;
            }
            case OR:
            {
                System.out.print("OR(");
                visitSubExpressions(expression);
                System.out.print(")");
                break;
            }
            case NOT:
            {
                System.out.print("NOT(");
                visitSubExpressions(expression);
                System.out.print(")");
                break;
            }
        }
        
        if ( thisIterIsFirst )
        {
            System.out.println();
        }
    }

    private boolean limitReached()
    {
        return ( thisMany > 0 ) && ( visited > thisMany );
    }

    private void visitSubExpressions(BooleanExpression expression)
    {
        boolean first=true;
        
        for(BooleanExpression b : expression.getSubExpressions())
        {
            if ( limitReached() )
            {
                return;
            }

            if ( ! first )
            {
                System.out.print(",");
            }
            else
            {
                first = false;
            }
            
            visit(b);
        }
    }

}
