package persistence;

import util.Pair;

import java.util.ArrayList;


public class Specification{

    private final String wheres;
    private final String joins;
    private final String orderBy;
    private final ArrayList<Pair<Object, Integer>> params;
    private final int limit;
    private final int offset;

    public Specification(String wheres, String joins, String orderBy,
                         ArrayList<Pair<Object, Integer>> params,
                         int limit, int offset) {
        this.wheres = wheres;
        this.joins = joins;
        this.orderBy = orderBy;
        this.params = params;
        this.limit = limit;
        this.offset = offset;
    }

    public String getWheres() {
        return wheres;
    }

    public String getJoins() {
        return joins;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public ArrayList<Pair<Object, Integer>> getParams() {
        return params;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
