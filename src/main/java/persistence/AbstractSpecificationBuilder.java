package persistence;

import util.Pair;

import java.util.ArrayList;

public abstract class AbstractSpecificationBuilder <T extends AbstractMapper<T>>{
    protected String wheres;
    protected String joins;
    protected final ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    protected boolean ascending = true;
    protected String orderBy;
    private int limit = 50;
    private int offset = 0;

    protected abstract T getThisBuilder();
    protected abstract Specification build();

    public T ascendingOrder(){
        ascending = true;
        return getThisBuilder();
    }

    public T descendingOrder(){
        ascending = false;
        return getThisBuilder();
    }

    public T setLimit(int limit){
        this.limit = limit;
        return getThisBuilder();
    }

    public T setOffset(int offset){
        this.offset = offset;
        return getThisBuilder();
    }
}
