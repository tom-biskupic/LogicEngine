package com.runcible.logicengine.logic;

public class IsVisitedMemento implements Memento
{
    public IsVisitedMemento(boolean visited)
    {
        this.isVisited = visited;
    }
    
    public boolean isVisited()
    {
        return this.isVisited;
    }
    
    private boolean isVisited=false;

}
