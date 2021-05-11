package persistence;

import util.Pair;

import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class Specification {
    public StringJoiner joins = new StringJoiner("\n");
    public StringJoiner wheres = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
    public ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    public String sortBy;
    public String sortOrder = "ASC";
    public int limit = 50;
    public int offset = 0;

    protected abstract Specification byId(int id);

    protected Specification(String defaultSortBy) {
        this.sortBy = defaultSortBy;
    }

    public Specification ascendedOrder(){
        sortOrder = "ASC";
        return this;
    }

    public Specification descenderOrder(){
        sortOrder = "DESC";
        return this;
    }

    public Specification setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public Specification setOffset(int offset){
        this.offset = offset;
        return this;
    }
}
