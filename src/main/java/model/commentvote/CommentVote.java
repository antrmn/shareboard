package model.commentvote;

import model.comment.Comment;
import model.user.User;

public class CommentVote {
    public static final short DOWNVOTE = -1;
    public static final short UPVOTE = +1;

    private User user;
    private Short vote;
    private Comment comment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Short getVote() {
        return vote;
    }

    public void setVote(Short vote) {
        this.vote = vote;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
