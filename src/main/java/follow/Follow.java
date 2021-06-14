package follow;

import comment.Comment;
import section.Section;
import user.User;

import java.time.Instant;
import java.util.List;

public class Follow {

    private User user;
    private Section section;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
