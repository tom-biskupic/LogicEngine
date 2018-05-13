package com.runcible.logicengine.logic;

import java.util.ArrayList;
import java.util.List;

public class Evaluator implements BooleanExpressionVisitor
{

    @Override
    public void visit(BooleanExpression expression)
    {
        try
        {
            if ( isEvaluated(expression) )
            {
                return;
            }
     
            visited++;
            
            depth++;
            if ( maxDepth < depth )
            {
                maxDepth = depth;
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
        
        setEvaluated(expression,true);
        
        depth--;
    }

    /**
     * Returns true if the Boolean expression has already been evaluated
     * @param expression The expression to test
     * @return true if the expression has already been evaluated.
     */
    private boolean isEvaluated(BooleanExpression expression)
    {
        IsVisitedMemento memento = (IsVisitedMemento) expression.getMemento();
        return ( memento != null && memento.isVisited());
    }
    
    /**
     * Marks the boolean expression as having been evaluated
     * @param expression The expression to modify
     * @param value true if the expression has been evaluated and false otherwise
     */
    private void setEvaluated(BooleanExpression expression,boolean value)
    {
        expression.setMemento( new IsVisitedMemento(true));
    }

    private void evaluateNot(BooleanExpression expression) throws NotConstantError
    {
        BooleanExpression subExpression = expression.getSubExpressions().get(0);
        
        if ( ! subExpression.getBooleanValue().hasValue() )
        {
            visit(subExpression);
        }

        if ( subExpression.getBooleanValue().hasValue() )
        {
            evaluatedToConst++;
            expression.setBooleanValue(new BooleanValue(!subExpression.getBooleanValue().getValue()));
        }
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
        //
        //  Do this in two runs - first check for constant sub-expresions
        //  that we can use to eliminate the whole expression
        //
        List<BooleanExpression> toBeReduced = new ArrayList<BooleanExpression>();
        
        for( BooleanExpression b : expression.getSubExpressions() )
        {
            if ( b.getBooleanValue().hasValue() )
            {
                if ( reduceIfPossible(expression, b, lookfor))
                {
                    evaluatedToConst++;
                    return;
                }
            }
            else
            {
                toBeReduced.add(b);
            }
        }

        boolean nonConstSubExpr = false;
        
        for(BooleanExpression b : toBeReduced )
        {
            visit(b);

            if ( b.getBooleanValue().hasValue() )
            {
                if ( reduceIfPossible(expression, b, lookfor))
                {
                    evaluatedToConst++;
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
            evaluatedToConst++;
            expression.getBooleanValue().setValue(!lookfor);
        }
    }

    private boolean reduceIfPossible(
            BooleanExpression   expression, 
            BooleanExpression   subExpression,
            boolean             lookFor) throws NotConstantError
    {
        if ( subExpression.getBooleanValue().getValue() == lookFor )
        {
            expression.setBooleanValue( new BooleanValue(lookFor));
            return true;
        }
        
        return false;
    }

    public String statsAsString()
    {
        String stats = "Evaulator stats:\n";
        stats += "visited="+visited+"\n";
        stats += "evaluated to const="+evaluatedToConst+"\n";
        stats += "Max expression depth="+maxDepth+"\n";
        return stats;
    }

    private int maxDepth=0;
    private int depth=0;
    private int visited=0;
    private int evaluatedToConst=0;
}

