-- Password is 1234
INSERT INTO app_user (name, password, email, role)
VALUES ('Joe User', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joeuser@mail.com', 'CANDIDATE');

INSERT INTO app_user (name, password, email, role)
VALUES ('Joe Admin', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joeadmin@mail.com', 'ADMIN');

INSERT INTO app_user (name, password, email, role)
VALUES ('Joe Company', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joecompany@mail.com', 'COMPANY');

INSERT INTO app_user_profile( app_user_id,education,skill,work,course,
                              phone, volunteering,project, award,language)
VALUES (1,'Engineeing', 'fast learner', 'developer', 'ML','5144444444','sss','xyz','CARE','ARABIC');


INSERT INTO company_profile(app_user_id, website,industry, company_size,location,speciality)
VALUES (3,'www.google.com', 'technology', '150k', 'Newyork','search');