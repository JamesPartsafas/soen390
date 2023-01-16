-- Password is 1234
INSERT INTO app_user (id, name, password, email, role)
VALUES (1, 'Joe User', '$2a$12$5E0cSCs0SlULsDfdhsYgxObMbnAbXbIC/gge/uM5U3CjTW2/UFF8.', 'joeuser@mail.com', 'USER');

INSERT INTO app_user (id, name, password, email, role)
VALUES (2, 'Joe Admin', '$2a$12$5E0cSCs0SlULsDfdhsYgxObMbnAbXbIC/gge/uM5U3CjTW2/UFF8.', 'joeadmin@mail.com', 'ADMIN');

