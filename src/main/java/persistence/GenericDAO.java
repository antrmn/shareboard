package persistence;

import util.Pair;
import util.SetNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public abstract class GenericDAO<T, M extends AbstractMapper<T>> {

    protected Connection con;

    private final List<String> readableColumnsList;
    private final List<String> updatableColumnsList;
    private final String view;
    private final String table;
    private final M mapper;

    protected GenericDAO(Connection con, List<String> readableColumnsList, List<String> updatableColumnsList, String view, String table, M mapper){
        this.con = con;
        this.readableColumnsList = readableColumnsList;
        this.updatableColumnsList = updatableColumnsList;
        this.view = view;
        this.table = table;
        this.mapper = mapper;
    }

    protected abstract String fillUpdateStatement(T bean, List<Pair<Object, Integer>> params);
    protected abstract void fillInsertStatement(T bean, List<Pair<Object, Integer>> params);

    public List<T> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM %s AS %s %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, String.join(",",readableColumnsList),
                             view,
                             table,
                             specification.getJoins(),
                             specification.getWheres(),
                             specification.getOrderBy()
        );


        List<Pair<Object,Integer>> params = specification.getParams();
        PreparedStatement ps = con.prepareStatement(query);
        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }
        ps.setInt(i++, specification.getLimit());
        ps.setInt(i, specification.getOffset());

        ResultSet rs = ps.executeQuery();
        ArrayList<T> beans = new ArrayList<>();


        while(rs.next()){
            beans.add(mapper.toBean(rs));
        }
        rs.close();
        return beans;
    }

    public int update(T bean) throws SQLException {
        String statement = "UPDATE %s SET %s WHERE id = ?";

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        String valuesToSet = fillUpdateStatement(bean, params);

        statement = String.format(statement, table, valuesToSet);

        if(valuesToSet.isBlank() || params.isEmpty()){
            throw new RuntimeException("Empty SET clause");
        }

        statement = String.format(statement,valuesToSet);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){ //NOTA: l'ultimo param deve essere l'id.
            if(param.getLeft() == SetNull.NULL){
                ps.setNull(i++, param.getRight());
            } else{
                ps.setObject(i++, param.getLeft(), param.getRight());
            }
        }

        return ps.executeUpdate();
    }

    public int create(List<T> beans) throws SQLException {
        String statement = "INSERT INTO %s (%s) VALUES %s";

        StringJoiner _questionMarks = new StringJoiner(",","(",")");
        for(int i=0; i<updatableColumnsList.size(); i++)
            _questionMarks.add("?");
        String questionMarks = _questionMarks.toString();

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner values = new StringJoiner(",");
        for(T bean : beans){
            fillInsertStatement(bean, params);
            values.add(questionMarks);
        }

        if(values.length()==0){
            throw new RuntimeException("Empty VALUES clause");
        }

        statement = String.format(statement, table,
                                             String.join(",",updatableColumnsList),
                                             values);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }

        return ps.executeUpdate();
    }

    public int delete(int[] ids) throws SQLException {
        String statement = "DELETE FROM %s WHERE id IN (%s)";

        StringJoiner sj = new StringJoiner(",");
        for (int ignored : ids){
            sj.add("?");
        }

        statement = String.format(statement, table, sj);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (int id : ids){
            ps.setInt(i++, id);
        }

        return ps.executeUpdate();
    }

    public int create(T bean) throws SQLException {
        return create(List.of(bean));
    }

    public int delete(int id) throws SQLException {
        return delete(new int[]{id});
    }
}