package post;

import util.Pair;

import java.sql.Types;
import java.util.ArrayList;
import java.time.Instant;
import java.util.StringJoiner;

public class PostSpecification {

    private static final String column = "id, title, content, type, creation_date, " +
            "author_id, username, is_admin, section_id, section_name, " +
            "votes, n_comments";

    private static final String columnLogged = column + ", v1.vote";

    StringJoiner joins = new StringJoiner("\n");
    StringJoiner wheres = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
    ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    String sortBy = "post.creation_date";
    String sortOrder = "ASC";
    String columnsToRetrieve = column;
    int limit = 50;
    int offset = 0;

    public PostSpecification loggedUser(Integer id){
        joins.add("LEFT JOIN (SELECT" +
                                " post_id, vote, user_id" +
                                " FROM post_vote " +
                                " JOIN user " +
                                " ON user_id=user.id " +
                                " WHERE user_id=?)" +
                " AS v1 " +
                "ON v1.post_id = post.id");
        columnsToRetrieve = columnLogged;
        params.add(0, new Pair<>(id, java.sql.Types.INTEGER)); //nota: aggiunto all'inizio della lista perchè è nel join.
        return this;
    }

    public PostSpecification isVotedBy(int id){
        joins.add("INNER JOIN (post_vote as pv) ON pv.post_id=pv ");
        wheres.add("user_id=?");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecification isSectionFollowedByUser(int id){
        joins.add("INNER JOIN (follow as f) ON f.section_id = sec.id ");
        wheres.add("f.user_id=?");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecification isInSection(int id){
        wheres.add("sec.id = ? ");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecification isAuthor(int id){
        wheres.add("author_id = ? ");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecification isAuthor(String name){
        wheres.add("user.username = ? ");
        params.add(new Pair<>(name, java.sql.Types.VARCHAR));
        return this;
    }

    public PostSpecification doesTitleContains(String title){
        wheres.add("post.title LIKE %?%");
        params.add(new Pair<>(title, java.sql.Types.VARCHAR));
        return this;
    }

    public PostSpecification doesBodyContains(String content){
        wheres.add("post.content LIKE %?%");
        params.add(new Pair<>(content, java.sql.Types.VARCHAR));
        return this;
    }

    public PostSpecification doesTitleOrBodyContains(String content){ //Todo: implement OR and delete this
        wheres.add(("post.title LIKE %?% OR post.content LIKE %?%"));
        return this;
    }

    public PostSpecification isOlderThan(Instant date){
        wheres.add("post.creation_date >= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public PostSpecification isNewerThan(Instant date){
        wheres.add("post.creation_date <= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public PostSpecification isType(Post.Type type){
        wheres.add("post.type = ?");
        params.add(new Pair<>(type.name(), Types.VARCHAR));
        return this;
    }

    public PostSpecification byId(int id){
        wheres.add("id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public PostSpecification sortByTime(){
        sortBy = "post.creation_date";
        return this;
    }

    public PostSpecification sortByVotes(){
        sortBy = "votes";
        return this;
    }

    public PostSpecification ascendedOrder(){
        sortOrder = "ASC";
        return this;
    }

    public PostSpecification descenderOrder(){
        sortOrder = "DESC";
        return this;
    }

    public PostSpecification setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public PostSpecification setOffset(int offset){
        this.offset = offset;
        return this;
    }
}
