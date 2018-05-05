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
                    evaluateTerm(expression);
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

    private void evaluateTerm(BooleanExpression expression)
    {
        // TODO Auto-generated method stub
        
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
        for( BooleanExpression b : expression.getSubExpressions() )
        {
            if ( ! b.getBooleanValue().hasValue() )
            {
                visit(b);
            }
            
            if ( b.getBooleanValue().getValue() )
            {
                expression.setBooleanValue( new BooleanValue(true));
                return;
            }
        }
        
        expression.setBooleanValue(new BooleanValue(false));
    }

    private void evaluateAnd(BooleanExpression expression) throws NotConstantError
    {
        for( BooleanExpression b : expression.getSubExpressions() )
        {
            if ( ! b.getBooleanValue().hasValue() )
            {
                visit(b);
            }

            if ( ! b.getBooleanValue().getValue() )
            {
                expression.setBooleanValue(new BooleanValue(false));
                return;
            }
        }
        
        expression.setBooleanValue(new BooleanValue(true));
    }

}
