insert into shareboard.category (id, description, name)
values  (1, 'Descrizione1', 'Sezione1'),
        (2, 'Descrizione2', 'Sezione2');

insert into shareboard.post (post_id, category_id, author_id, title, text, type, creation_date)
values  (2, 1, 5, 'Post1', ' Fusce ultricies sagittis pharetra. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Quisque nunc leo, ultrices sit amet pretium sit amet, lacinia at nisi. Integer faucibus vitae augue eu porttitor. Pellentesque rhoncus neque ac purus vulputate vulputate. Donec vel mauris ac odio vestibulum mollis. In porta sollicitudin eros. In hac habitasse platea dictumst. Integer maximus diam eu leo varius ultrices. Aliquam sit amet ex a ex mattis posuere. Aenean scelerisque nisl sit amet arcu rhoncus posuere. Fusce viverra, erat quis luctus dignissim, magna turpis imperdiet tortor, non sodales enim purus ac velit. Morbi ornare leo sit amet aliquam condimentum. Praesent a ultrices ex, eu aliquet diam. Mauris quis malesuada libero, ac dignissim tellus. Nunc non erat justo.', 'TEXT', '2021-03-30 12:49:31'),
        (3, 2, 4, 'Post2', 'Vivamus quis pharetra metus. Maecenas tempor molestie convallis. Fusce sit amet risus eget nibh ultrices dictum. Sed porta viverra tellus, in dignissim massa rhoncus id. Sed porttitor purus eget mi vehicula euismod. Quisque nec auctor lorem, at sagittis nisi. Nulla massa urna, rhoncus sed ex hendrerit, commodo consequat ipsum. Suspendisse iaculis dui vel convallis porttitor. Nam diam lorem, facilisis nec ultrices placerat, malesuada vel augue. Suspendisse metus quam, consectetur in dui sed, accumsan volutpat orci. Proin maximus enim id facilisis elementum. Nunc gravida lorem quis felis fermentum, at blandit dui dapibus. Nulla elementum eros sed dolor tempus, in luctus felis ornare. Pellentesque dignissim metus et aliquet semper. Curabitur porta est ut luctus tincidunt. Curabitur vitae consequat libero. ', 'TEXT', '2021-03-30 12:49:31'),
        (4, 1, 3, 'Post3', 'Vestibulum a turpis eget turpis porttitor laoreet at vitae ex. Nam fermentum ante magna, eu fringilla lorem egestas feugiat. Nulla id varius odio, nec commodo risus. Phasellus at quam in ipsum interdum tempus. Nullam quis ultricies sem. Quisque commodo pharetra diam sed vulputate. Cras velit orci, pharetra sit amet facilisis id, lacinia id mi. Integer iaculis ac lectus vitae semper. ', 'TEXT', '2021-03-30 12:49:31'),
        (5, 2, 1, 'Post4', 'Testo', 'TEXT', '2021-03-30 12:49:31'),
        (6, 1, 3, 'Post5', 'Testo', 'TEXT', '2021-03-30 12:49:31'),
        (7, 2, 2, 'Post6', 'Testo', 'TEXT', '2021-03-30 12:49:31');

insert into shareboard.user (id, username, password, email, creation_date)
values  (1, 'Utente1', 'password1', 'utente1@email.it', '2021-03-30 12:41:26'),
        (2, 'Utente2', 'password2', 'utente2@email.it', '2021-03-30 12:41:26'),
        (3, 'Utente3', 'password3', 'utente3@email.it', '2021-03-30 12:41:26'),
        (4, 'Utente4', 'password4', 'utente4@email.it', '2021-03-30 12:41:26'),
        (5, 'Utente5', 'password5', 'utente5@email.it', '2021-03-30 12:41:26');
