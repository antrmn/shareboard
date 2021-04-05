use shareboard;

SELECT post.post_id, post.title, post.text, post.type, post.creation_date, user.id, user.username, category.id, category.name, SUM(postvotes.vote) AS voto
FROM post
LEFT JOIN user ON post.author_id=user.id
INNER JOIN category ON post.category_id=category.id 
LEFT JOIN postvotes ON post.post_id = postvotes.post_id
GROUP BY post.post_id;