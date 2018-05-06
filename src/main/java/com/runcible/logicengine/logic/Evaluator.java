package com.runcible.logicengine.logic;

public class Evaluator implements BooleanExpressionVisitor
{

    @Override
    public void visit(BooleanExpression expression)
    {
        try
        {
            if ( expression.getBooleanValue().hasValue() )
            {
                return;
            }
            
            switch(expression.getType())
            {
                case AND:
                {
                    evaluateAnd(expression);
                    break;
                }
                case OR:
                {
                    evaluateOr(expression);
                    break;
                }
                case NOT:
                {
                    evaluateNot(expression);
                    break;
                }
                case TERM:
                {
                    //
                    //  Nothing to do here as it is already evaluated
                    //
                    break;
                }
            }
        }
        catch( NotConstantError e)
        {
            //
            //  Can't evaluate if not constant so just leave it
            //
        }
    }

    private void evaluateNot(BooleanExpression expression) throws NotConstantError
    {
        BooleanExpression subExpression = expression.getSubExpressions().get(0);
        
        if ( ! subExpression.getBooleanValue().hasValue() )
        {
            visit(subExpression);
        }

        expression.setBooleanValue(new BooleanValue(!subExpression.getBooleanValue().getValue()));
    }

    private void evaluateOr(BooleanExpression expression) throws NotConstantError
    {
        evaluateAndOr(expression,true);
    }

    private void evaluateAnd(BooleanExpression expression) throws NotConstantError
    {
        evaluateAndOr(expression,false);
    }

    /**
     * This will evaluate an and or an or expression. This looks for 
     * a sub-expression with a particular value and if it finds one
     * the overall expression takes gets that value.
     * @param expression
     * @throws NotConstantError 
     */
    private void evaluateAndOr(BooleanExpression expression, boolean lookfor) throws NotConstantError
    {
        boolean nonConstSubExpr = false;
        
        for( BooleanExpression b : expression.getSubExpressions() )
        {
            if ( ! b.getBooleanValue().hasValue() )
            {
                visit(b);
            }
            
            if ( b.getBooleanValue().hasValue() )
            {
                if ( b.getBooleanValue().getValue() == lookfor )
                {
                    expression.setBooleanValue( new BooleanValue(lookfor));
                    return;
                }
            }
            else
            {
                nonConstSubExpr = true;
            }
        }
        
        if ( nonConstSubExpr )
        {
            expression.getBooleanValue().setHasNoValue();
        }
        else
        {
            expression.getBooleanValue().setValue(!lookfor);
        }
    }
}
