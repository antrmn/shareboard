package persistence;

import util.Pair;

import java.util.ArrayList;


/**
 * Fornisce le specifiche per formattare un SELECT statement.
 * Per istanziare un oggetto di questo tipo, occorre usare la classe {@link #Specification(Builder)}
 */
public class Specification{
    /**
     * La classe per istanziare oggetti di tipo Specification
     * @param <T> Il nome della classe che estende {@link Builder}
     */
    public static abstract class Builder <T extends Builder<T>> {
        protected String wheres;
        protected String joins;
        protected final ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        protected boolean ascending = true;
        protected String orderBy;
        protected int limit = 50;
        protected int offset = 0;

        /**
         * Esegue "return this".
         * @return this.
         */
        protected abstract T getThisBuilder();

        /**
         * Istanzia un oggetto di tipo {@link Specification}
         * @return Un'istanza di Specification
         */
        protected Specification build(){
            return new Specification(this);
        }

        public T ascendingOrder() {
            ascending = true;
            return getThisBuilder();
        }

        public T descendingOrder() {
            ascending = false;
            return getThisBuilder();
        }

        public T setLimit(int limit) {
            this.limit = limit;
            return getThisBuilder();
        }

        public T setOffset(int offset) {
            this.offset = offset;
            return getThisBuilder();
        }
    }

    private final String wheres;
    private final String joins;
    private final String orderBy;
    private final ArrayList<Pair<Object, Integer>> params;
    private final int limit;
    private final int offset;

    private Specification(Builder<? extends Builder<?>> builder) {
        wheres = builder.wheres;
        joins = builder.joins;
        params = builder.params;
        limit = builder.limit;
        offset = builder.offset;
        if(builder.orderBy != null && !builder.orderBy.isBlank())
            orderBy = " ORDER BY " + builder.orderBy + " " + (builder.ascending ? "ASC":"DESC");
        else
            orderBy = "";
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
