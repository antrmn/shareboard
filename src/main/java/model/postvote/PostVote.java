package model.postvote;

import model.post.Post;
import model.user.User;

public class PostVote {
    public static final short DOWNVOTE = -1;
    public static final short UPVOTE = +1;

    private User user;
    private Short vote;
    private Post post;

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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
