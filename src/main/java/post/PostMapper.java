package post;

import persistence.AbstractMapper;
import section.SectionMapper;
import user.UserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;

public class PostMapper extends AbstractMapper<Post> {

    public PostMapper() {
        super(Map.of(
                "post_id",             (p,o) -> p.setId((Integer)   o),
                "post_title",          (p,o) -> p.setTitle((String) o),
                "post_content",        (p,o) -> p.setContent((String) o),
                "post_type",           (p,o) -> p.setType(Post.Type.valueOf((String) o)),
                "post_creation_date",  (p,o) -> p.setCreationDate((Instant) o),
                "post_votes",          (p,o) -> p.setVotes((Integer) o),
                "post_n_comments",     (p,o) -> p.setnComments((Integer) o),
                "post_vote",           (p,o) -> p.setVote((Integer) o)
        ));
    }

    @Override
    protected Post instantiate() {
        return new Post();
    }

    @Override
    public Post toBean(ResultSet rs) throws SQLException {
        Post p = super.toBean(rs);
        UserMapper um = new UserMapper();
        p.setAuthor(um.toBean(rs));
        SectionMapper sm = new SectionMapper();
        p.setSection(sm.toBean(rs));
        return p;
    }
}
