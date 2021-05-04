WITH RECURSIVE Recurse_Comments AS (
    SELECT * FROM comment WHERE parent_comment_id IS NULL
    UNION ALL
    SELECT com.* FROM comment AS com JOIN Recurse_Comments AS r ON com.parent_comment_id = r.id
)
SELECT * FROM Recurse_Comments;