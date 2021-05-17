package comment;


import persistence.AbstractMapper;
import persistence.SQL_TriConsumer;
import post.Post;
import user.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;


public class CommentMapper implements AbstractMapper<Comment> {
    static Map<String, SQL_TriConsumer<Comment>> mapComment = new HashMap<>(){{
        put("id",                (c,s,rs) -> c.setId(rs.getInt(s)));
        put("content",           (c,s,rs) -> c.setText(rs.getString(s)));
        put("creation_date",     (c,s,rs) -> c.setCreationDate(rs.getTimestamp(s).toInstant()));
        put("votes",             (c,s,rs) -> c.setVotes(rs.getInt(s)));
        put("vote",              (c,s,rs) -> c.setVote(rs.getInt(s)));
    }};

    static Map<String, SQL_TriConsumer<Post>> mapPost = new HashMap<>(){{
        put("post_id",           (p,s,rs) -> p.setId(rs.getInt(s)));
        put("post_title",        (p,s,rs) -> p.setTitle(rs.getString(s)));
    }};

    static Map<String, SQL_TriConsumer<User>> mapAuthor = new HashMap<>(){{
        put("author_id",         (u,s,rs) -> u.setId(rs.getInt(s)));
        put("author_username",   (u,s,rs) -> u.setUsername(rs.getString(s)));
    }};

    static Map<String, SQL_TriConsumer<Comment>> mapParentComment = new HashMap<>(){{
        put("parent_comment_id", (c,s,rs) -> c.setId(rs.getInt(s)));
    }};

    public List<Comment> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<Comment> beans = new ArrayList<>();
        HashMap<Integer, User> authors = new HashMap<>();
        HashMap<Integer, Post> posts = new HashMap<>();
        HashMap<Integer, Comment> parentComments = new HashMap<>();
        while (rs.next()) {
            Comment comment = new Comment();

            User author = null;
            if (columns.contains("author_id")) {
                author = authors.get(rs.getInt("author_id"));
                if (author == null) {
                    author = new User();
                    for (String column : columns) {
                        SQL_TriConsumer<User> consumer = mapAuthor.get(column);
                        if(consumer != null)
                            consumer.accept(author, column, rs);
                    }
                    authors.put(author.getId(), author);
                }
            }
            comment.setAuthor(author);

            Post post = null;
            if (columns.contains("post_id")) {
                post = posts.get(rs.getInt("post_id"));
                if (post == null) {
                    post = new Post();
                    for (String column : columns) {
                        SQL_TriConsumer<Post> consumer = mapPost.get(column);
                        if(consumer != null)
                            consumer.accept(post, column, rs);
                    }
                    posts.put(post.getId(), post);
                }
            }
            comment.setPost(post);

            Comment parentComment = null;
            if (columns.contains("parent_comment_id")) {
                parentComment = parentComments.get(rs.getInt("parent_comment_id"));
                if (parentComment == null) {
                    parentComment = new Comment();
                    for (String column : columns) {
                        SQL_TriConsumer<Comment> consumer = mapParentComment.get(column);
                        if(consumer != null)
                            consumer.accept(parentComment, column, rs);
                    }
                    parentComments.put(parentComment.getId(), parentComment);
                }
            }
            comment.setParentComment(parentComment);

            for (String column : columns) {
                SQL_TriConsumer<Comment> consumer = mapComment.get(column);
                if(consumer != null)
                    consumer.accept(comment, column, rs);
            }
            beans.add(comment);
        }
        return beans;
    }

    public Map<Integer, ArrayList<Comment>> toBeansHierarchy(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        HashMap<Integer, ArrayList<Comment>> comments = new HashMap<>();
        HashMap<Integer, User> authors = new HashMap<>();
        HashMap<Integer, Post> posts = new HashMap<>();
        while(rs.next()) {
            Comment comment = new Comment();

            User author = null;
            if (columns.contains("author_id")) {
                author = authors.get(rs.getInt("author_id"));
                if (author == null) {
                    author = new User();
                    for (String column : columns) {
                        SQL_TriConsumer<User> consumer = mapAuthor.get(column);
                        if(consumer != null)
                            consumer.accept(author, column, rs);
                    }
                    authors.put(author.getId(), author);
                }
            }
            comment.setAuthor(author);

            Post post = null;
            if (columns.contains("post_id")) {
                post = posts.get(rs.getInt("post_id"));
                if (post == null) {
                    post = new Post();
                    for (String column : columns) {
                        SQL_TriConsumer<Post> consumer = mapPost.get(column);
                        if(consumer != null)
                            consumer.accept(post, column, rs);
                    }
                    posts.put(post.getId(), post);
                }
            }
            comment.setPost(post);

            for (String column : columns) {
                SQL_TriConsumer<Comment> consumer = mapComment.get(column);
                if(consumer != null)
                    consumer.accept(comment, column, rs);
            }

            int parentCommentId = rs.getInt("parent_comment_id");
            if(!comments.containsKey(parentCommentId)){
                comments.put(parentCommentId, new ArrayList<>());
            }
            comments.get(parentCommentId).add(comment);


            Comment nullComment = new Comment();
            nullComment.setId(0);
            Stack<Comment> stack = new Stack<>();
            List<Comment> rootComments = comments.get(0);
            if(rootComments != null)
                rootComments.forEach(c -> {
                                                c.setParentComment(nullComment);
                                                stack.add(c);
                                            });
            while(!stack.isEmpty()) {
                Comment parent = stack.pop();
                List <Comment> parentsOf = comments.get(parent.getId());
                if(parentsOf != null)
                    parentsOf.forEach(c -> {
                                            stack.add(c);
                                            c.setParentComment(parent);
                                         });
            }
        }
        return comments;
    }
}