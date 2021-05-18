package section;

import user.User;
import post.Post;

import java.util.Collection;
import java.util.List;

public class Section {
    private Integer id;
    private String name;
    private String description;
    private String picture;
    private Integer nFollowers;
    private Boolean isFollowed;

    private Collection<User> followers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getnFollowers() {
        return nFollowers;
    }

    public void setnFollowers(Integer nFollowers) {
        this.nFollowers = nFollowers;
    }

    public Boolean getFollowed() {
        return isFollowed;
    }

    public void setFollowed(Boolean followed) {
        isFollowed = followed;
    }

    public Collection<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Collection<User> followers) {
        this.followers = followers;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    private List<Post> posts;

}
