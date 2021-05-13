<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 13/05/2021
  Time: 09:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Find post</title>
</head>
<body>
<form action="findPosts" id="findPostsForm" method="get">
    Entra da utente loggato (id): <input type="text" name="loggedUser"><br><br><br>
    Post votato da (id): <input type="text" name="votedBy"><br>
    Post sezioni seguite da (id):  <input type="text" name="sectionFollowedBy"><br>
    Sezione (id): <input type="text" name="section"><br>
    Autore (id): <input type="text" name="author"><br>
    Autore (nome): <input type="text" name="authorName"><br>
    Cerca per titolo: <input type="text" name="titleContains"><br>
    Cerca per contenuto: <input type="text" name="contentContains"><br>
    Creati prima di: <input type="date" name="olderThan"><br>
    Creati dopo di: <input type="date" name="newerThan"><br>
    Tipo post: <select name="type" form="findPostsForm">
                    <option value=" " disabled selected></option>
                    <option value="TEXT">Testo</option>
                    <option value="IMG">Immagine</option>
               </select><br>
    Id post: <input type="id" name="idPost"><br><br>

    Ordina per: <select name="orderBy" form="findPostsForm">
                    <option value=" " disabled selected></option>
                    <option value="tempo">Tempo</option>
                    <option value="voti">Voti</option>
                </select><input type="checkbox" name="descending" value="true">Ordine decrescente?<br>

    Limite: <input type="text" name="limit"><br>
    Offset: <input type="text" name="offset"><br>

    <input type="submit" value="cerca">
</form>
</body>
</html>
