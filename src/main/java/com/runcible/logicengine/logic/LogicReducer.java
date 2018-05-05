package com.runcible.logicengine.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogicReducer implements BooleanExpressionVisitor
{
    private class LogicReducerMemento implements Memento
    {
        public LogicReducerMemento(boolean reduced)
        {
            this.isReduced = reduced;
        }
        
        boolean isReduced=false;
    }
    
    private boolean isReduced(BooleanExpression expression)
    {
        LogicReducerMemento memento = (LogicReducerMemento) expression.getMemento();
        return ( memento != null && memento.isReduced );
    }
    
    private void setReduced(BooleanExpression expression,boolean value)
    {
        expression.setMemento( new LogicReducerMemento(true));
    }
    
    @Override
    public void visit(BooleanExpression expression)
    {
        if ( isReduced(expression) )
        {
            return;
        }
        
        visited++;
        
        depth++;
        if ( maxDepth < depth )
        {
            maxDepth = depth;
        }
        
        for(BooleanExpression e : expression.getSubExpressions() )
        {
            visit(e);
        }
        
        switch( expression.getType() )
        {
            case AND:
                reduceAnd(expression);
                break;
            case OR:
                reduceOr(expression);
                break;
            case NOT:
                reduceNot(expression);
                break;
            case TERM:
                break;
        }
        
        setReduced(expression,true);
        
        depth--;
    }

    private void reduceAnd(BooleanExpression expression)
    {
        andOrReducer(expression,false);
        
        //
        //  If the expression didn't get totally squashed then see if we can merge
        //  any sub-expressions into it
        //
        lookForExpressionsToMerge(expression);
    }

    private void reduceOr(BooleanExpression expression)
    {
        andOrReducer(expression,true);
        
        //
        //  If the expression didn't get totally squashed then see if we can merge
        //  any sub-expressions into it
        //
        lookForExpressionsToMerge(expression);
    }
    
    private void reduceNot(BooleanExpression expression)
    {
        try
        {
            BooleanExpression subExpression = expression.getSubExpressions().get(0);
            
            if ( subExpression.getBooleanValue().hasValue() )
            {
                reduced++;
                constantNotRemoved++;
                makeConstant(expression, new BooleanValue(!subExpression.getBooleanValue().getValue()));
            }
            else if ( subExpression.getType() == ExpressionType.NOT )
            {
                //
                //  So we have NOT(NOT(X)) == X
                //
                reduced++;
                notNotRemoved++;

                expression.replaceWith(subExpression.getSubExpressions().get(0));
            }
        }
        catch (NotConstantError e)
        {
            //
            //  Should not happen
            //
            e.printStackTrace();
        }
    }

    private void lookForExpressionsToMerge(BooleanExpression expression)
    {
        boolean modsMade = false;
        
        do
        {
            modsMade = false;

            List<BooleanExpression> newSubExpressionList = new ArrayList<BooleanExpression>();
            
            for ( BooleanExpression subExpression : expression.getSubExpressions())
            {
                if ( subExpression.getType().equals(expression.getType()))
                {
                    for (BooleanExpression subSubExpression : subExpression.getSubExpressions() )
                    {
                        newSubExpressionList.add(subSubExpression);
                    }
                    
                    modsMade = true;
                    reduced++;
                    subExprMerged++;
                }
                else if ( subExpression.getType().equals(ExpressionType.NOT) )
                {
                    BooleanExpression subSub = subExpression.getSubExpressions().get(0);
                    
                    if (    (   expression.getType().equals(ExpressionType.AND) 
                                && 
                                subSub.getType().equals(ExpressionType.OR))
                            ||
                            (   expression.getType().equals(ExpressionType.OR) 
                                && 
                                subSub.getType().equals(ExpressionType.AND)) )
                    {
                        for( BooleanExpression subSubSub : subSub.getSubExpressions() )
                        {
                            newSubExpressionList.add(invert(subSubSub));
                        }
                        
                        reduced++;
                        modsMade = true;
                        deMorgans++;
                    }
                    else
                    {
                        newSubExpressionList.add(subExpression);
                    }
                }
                else
                {
                    newSubExpressionList.add(subExpression);
                }
            }
            
            if ( modsMade )
            {
                expression.setSubExpressions(newSubExpressionList);
            }
        } 
        while(modsMade);
    }

    private BooleanExpression invert(BooleanExpression expression)
    {
        //
        //  Invert each expression. If it is already a NOT then just take the
        //  sub-expression. Otherwise add a NOT expresions
        //
        if ( expression.getType().equals(ExpressionType.NOT))
        {
            return expression.getSubExpressions().get(0);
        }
        else
        {
            return new BooleanExpression(ExpressionType.NOT,expression);
        }
    }

    private void makeConstant(BooleanExpression expression, BooleanValue value)
    {
        BooleanExpression newExpression = new BooleanExpression(ExpressionType.TERM);
        newExpression.setBooleanValue(value);
        setReduced(newExpression,true);
        expression.replaceWith( newExpression );
    }

    private void andOrReducer(BooleanExpression expression, boolean lookFor)
    {
        //
        //  If there is any expression which is false then the whole expression
        //  becomes a constant
        //
        try
        {
            if ( expression.getBooleanValue().hasValue())
            {
                reduced++;
                makeConstant(expression, expression.getBooleanValue());
            }
            else
            {
                boolean allSubsHaveValue = true;
                
                Set<BooleanExpression> newSubExpressionSet = new HashSet<BooleanExpression>();
                
                for( BooleanExpression b : expression.getSubExpressions())
                {
                    if ( b.getBooleanValue().hasValue() )
                    {
                        if ( b.getBooleanValue().getValue() == lookFor )
                        {
                            reduced++;
                            constantExpr++;
                            makeConstant(expression,b.getBooleanValue());
                            return;
                        }
                        else
                        {
                            //
                            //  Don't add to new list
                            //
                        }
                    }
                    else
                    {
                        if ( ! newSubExpressionSet.contains(b) )
                        {
                            //
                            //  See if this is a NOT of a previous expression
                            //
                            if ( b.getType() == ExpressionType.NOT )
                            {
                                if ( newSubExpressionSet.contains(b.getSubExpressions().get(0)) )
                                {
                                    if ( expression.getType() == ExpressionType.AND )
                                    {
                                        paradoxes++;
                                    }
                                    else
                                    {
                                        tautologies++;
                                    }
                                    
                                    makeConstant(expression, new BooleanValue(lookFor));
                                    return;
                                }
                            }   
                            
                            newSubExpressionSet.add(b);
                        }
                        else
                        {
                            duplicatesRemoved++;
                        }
                        
                        allSubsHaveValue = false;
                    }
                }
                
                //
                //  All the sub-expressions had a value and none of them was what we are looking for
                //  so the overall expression must be !lookFor
                //
                if ( allSubsHaveValue )
                {
                    reduced++;
                    constantExpr++;
                    makeConstant(expression,new BooleanValue(!lookFor));
                }
                else
                {
                    if ( newSubExpressionSet.size() == 1 )
                    {
                        reduced++;
                        singleSubExpr++;
                        expression.replaceWith(newSubExpressionSet.iterator().next());
                    }
                    else
                    {
                        expression.setSubExpressions(Arrays.asList(newSubExpressionSet.toArray(new BooleanExpression[0])));
                    }
                }
                
            }
        }
        catch (NotConstantError e)
        {
            //
            //  Shouldn't happen
            //
            e.printStackTrace();
        }
    }

    
    public int getVisited()
    {
        return visited;
    }

    public int getReduced()
    {
        return reduced;
    }

    public int getConstantNotRemovedCount()
    {
        return constantNotRemoved;
    }

    public int getNotNotRemovedCount()
    {
        return notNotRemoved;
    }

    public int getSubExpressionMergedCount()
    {
        return subExprMerged;
    }

    public int getSingleSubExpressionMergedCount()
    {
        return singleSubExpr;
    }
    
    public int getConstantExpressionsRemovedCount()
    {
        return constantExpr;
    }
    
    public void reset()
    {
        visited = 0;
        reduced = 0;
        constantNotRemoved = 0;
        notNotRemoved = 0;
        subExprMerged = 0;
        singleSubExpr = 0;
        constantExpr = 0;
        duplicatesRemoved = 0;
        maxDepth = 0;
        depth = 0;
        deMorgans = 0;
        tautologies = 0;
        paradoxes = 0;
    }
    
    public String statsAsString()
    {
        String stats = "Nodes visited="+visited;
        stats += "\nReduced="+reduced;
        stats += "\nConstant nots="+constantNotRemoved;
        stats += "\nNot nots removed="+notNotRemoved;
        stats += "\nSub expressions merged="+subExprMerged;
        stats += "\nSingle sub expressions merged="+singleSubExpr;
        stats += "\nConstant expressions="+constantExpr;
        stats += "\nDuplicates removed="+duplicatesRemoved;
        stats += "\nParadoxes="+paradoxes;
        stats += "\nTautologies="+tautologies;
        stats += "\nDeMorgans="+deMorgans;
        stats += "\nMax expression depth="+maxDepth;
        return stats;
    }
    
    private int visited = 0;
    private int reduced = 0;
    private int constantNotRemoved = 0;
    private int notNotRemoved = 0;
    private int subExprMerged=0;
    private int singleSubExpr=0;
    private int constantExpr=0;
    private int duplicatesRemoved=0;
    private int paradoxes=0;
    private int tautologies=0;
    private int deMorgans = 0;
    
    private int maxDepth=0;
    private int depth=0;

}
