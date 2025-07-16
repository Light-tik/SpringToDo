TRUNCATE TABLE todos RESTART IDENTITY CASCADE;

INSERT INTO todos (id, title, completed)
VALUES (1, 'Test Task 1', false),
       (2, 'Test Task 2', true),
       (3, 'Test Task 3', false);
