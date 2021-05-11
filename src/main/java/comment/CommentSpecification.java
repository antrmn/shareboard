package comment;

import post.Post;
import util.Pair;

import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class CommentSpecification {
    public static List<String> columns = List.of("id AS comment_id", "content AS comment_content", "creation_date AS comment_creation_date",
            "author_id AS user_id", "parent_comment_id", "username", "is_admin", "post_id", "post_title",
            "section_id", "section_name", "votes AS comment_votes", "v1.vote AS comment_vote");

    StringJoiner joins = new StringJoiner("\n");
    StringJoiner wheres = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");

    Integer loggedUserId = null;
    String loggedUserJoin = "CROSS JOIN (SELECT 0 AS vote) AS v1";

    ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    String sortBy = "comment_creation_date";
    String sortOrder = "ASC";

    int limit = 50;
    int offset = 0;

    public CommentSpecification loggedUser(Integer id){
        loggedUserJoin = "LEFT JOIN (SELECT" +
                                " comment_id, vote, user_id" +
                                " FROM comment_vote " +
                                " JOIN user " +
                                " ON user_id=user.id " +
                                " WHERE user_id=?)" +
                        " AS v1 " +
                        "ON v1.comment_id = comment_id";
        loggedUserId = id;
        return this;
    }

    public CommentSpecification isVotedBy(int id){
        joins.add("INNER JOIN comment_vote AS cv ON cv.comment_id=comment_id ");
        wheres.add("user_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentSpecification isAuthor(int id){
        wheres.add("user_id = ? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentSpecification isAuthor(String name){
        wheres.add("username = ? ");
        params.add(new Pair<>(name, Types.VARCHAR));
        return this;
    }

    public CommentSpecification doesBodyContains(String content){
        wheres.add("comment_content LIKE ?");
        params.add(new Pair<>('%'+content+'%', Types.VARCHAR));
        return this;
    }

    public CommentSpecification isOlderThan(Instant date){
        wheres.add("comment_creation_date >= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public CommentSpecification isNewerThan(Instant date){
        wheres.add("comment_creation_date <= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public CommentSpecification byId(int id){
        wheres.add("comment_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentSpecification sortByTime(){
        sortBy = "creation_date";
        return this;
    }

    public CommentSpecification sortByVotes(){
        sortBy = "votes";
        return this;
    }

    public CommentSpecification ascendedOrder(){
        sortOrder = "ASC";
        return this;
    }

    public CommentSpecification descenderOrder(){
        sortOrder = "DESC";
        return this;
    }

    public CommentSpecification setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public CommentSpecification setOffset(int offset){
        this.offset = offset;
        return this;
    }
}
