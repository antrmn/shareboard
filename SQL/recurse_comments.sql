/* Guest, vedi commenti post */
WITH RECURSIVE Recurse_Comments AS (
    SELECT
        0 AS depth
         , vcc.*
         , cte.vote
    FROM v_comment_complete AS vcc
             CROSS JOIN (SELECT 0 AS vote) AS cte
    WHERE parent_comment_id IS NULL
    UNION ALL
    SELECT
            depth+1 AS depth
         , com.*
         , cte.vote
    FROM v_comment_complete AS com
             CROSS JOIN (SELECT 0 AS vote) AS cte
             JOIN Recurse_Comments AS r
                  ON com.parent_comment_id = r.id
    WHERE depth <= ?
)
SELECT * FROM Recurse_Comments WHERE post_id = ?;


/* Guest, vedi risposte ai commenti */
WITH RECURSIVE Recurse_Comments AS (
    SELECT
        0 AS depth
        , vcc.*
        , cte.vote
    FROM v_comment_complete AS vcc
    CROSS JOIN (SELECT 0 AS vote) AS cte
    WHERE id = 71
    UNION ALL
        SELECT
            depth+1 AS depth
            , com.*
            , cte.vote
        FROM v_comment_complete AS com
        CROSS JOIN (SELECT 0 AS vote) AS cte
        JOIN Recurse_Comments AS r
            ON com.parent_comment_id = r.id
        WHERE depth <= 2
)
SELECT * FROM Recurse_Comments;


/* Logged user, vedi commenti a post */
WITH RECURSIVE Recurse_Comments AS (
    WITH votes_from_user_cte AS (
        SELECT
            comment_id
            , vote
            , user_id
        FROM comment_vote
        JOIN user
            ON user_id=user.id
        WHERE user_id=17
    )
    SELECT
        0 AS depth
        , vcc.*
        , cte.vote
    FROM v_comment_complete AS vcc
    LEFT JOIN votes_from_user_cte AS cte
        ON cte.comment_id = vcc.id
    WHERE parent_comment_id IS NULL
    UNION ALL
        SELECT
            depth+1 AS depth
            , com.*
            , cte.vote
        FROM v_comment_complete AS com
        LEFT JOIN votes_from_user_cte AS cte
            ON cte.comment_id = com.id
        JOIN Recurse_Comments AS r
            ON com.parent_comment_id = r.id
        WHERE depth <= 2
)
SELECT * FROM Recurse_Comments  WHERE post_id = 58;


/* logged user, vedi risposte ai commenti */
WITH RECURSIVE Recurse_Comments AS (
    WITH votes_from_user_cte AS (
        SELECT
            comment_id
            , vote
            , user_id
        FROM comment_vote
        JOIN user
            ON user_id=user.id WHERE user_id=17
    )
    SELECT
        0 AS depth
        , vcc.*
        , cte.vote
    FROM v_comment_complete AS vcc
    LEFT JOIN votes_from_user_cte AS cte
        ON cte.comment_id = vcc.id
    WHERE id = 73
    UNION ALL
        SELECT
            depth+1 AS depth
            , com.*
            , cte.vote
        FROM v_comment_complete AS com
        LEFT JOIN votes_from_user_cte AS cte
            ON cte.comment_id = com.id
        JOIN Recurse_Comments AS r
            ON com.parent_comment_id = r.id
        WHERE depth <= 2
)
SELECT * FROM Recurse_Comments;



/* CONCEPT */
WITH RECURSIVE Recurse_Comments AS (
    SELECT * FROM comment WHERE parent_comment_id IS NULL
    UNION ALL
    SELECT com.* FROM comment AS com JOIN Recurse_Comments AS r ON com.parent_comment_id = r.id
)
SELECT * FROM Recurse_Comments;