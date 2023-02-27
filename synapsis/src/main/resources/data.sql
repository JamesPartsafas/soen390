-- Password is 12345678, all security answers = "a"
INSERT INTO app_user (name, password, email, role, auth_provider, security_answer1, security_answer2, security_answer3)
VALUES ('Joe User', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joeuser@mail.com', 'CANDIDATE',
        'LOCAL', '$2a$10$6.8QMAk88DPFHH//rEchFOZpsYqe8eJYsx0bYHQuB1QCuEp69pf1q',
        '$2a$10$6.8QMAk88DPFHH//rEchFOZpsYqe8eJYsx0bYHQuB1QCuEp69pf1q',
        '$2a$10$6.8QMAk88DPFHH//rEchFOZpsYqe8eJYsx0bYHQuB1QCuEp69pf1q');

INSERT INTO app_user (name, password, email, role, auth_provider)
VALUES ('Joe Admin', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joeadmin@mail.com', 'ADMIN',
        'LOCAL');

INSERT INTO app_user (name, password, email, role, auth_provider)
VALUES ('Joe Company', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joecompany@mail.com', 'COMPANY',
        'LOCAL');

INSERT INTO app_user (name, password, email, role, auth_provider)
VALUES ('Joe Google', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joegoogle@mail.com', 'CANDIDATE',
        'GOOGLE');

INSERT INTO app_user (name, password, email, role, auth_provider)
VALUES ('Joe Recruiter', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joerecruiter@mail.com',
        'RECRUITER', 'LOCAL');

INSERT INTO connection (receiver_id, requester_id, pending)
VALUES (1, 2, false);

INSERT INTO connection (receiver_id, requester_id, pending)
VALUES (3, 1, false);

INSERT INTO connection (receiver_id, requester_id, pending)
VALUES (1, 4, true);
INSERT INTO app_user_profile( app_user_id, description, education,skill,work,course,
                              phone, volunteering,project, award,language)
VALUES (1, 'A standard user on Synapsis', 'Engineeing', 'fast learner', 'developer', 'ML','5144444444','sss','xyz','CARE','ARABIC');


INSERT INTO company_profile(app_user_id, description, website,industry, company_size,location,speciality)
VALUES (3,'A standard company on Synapsis', 'www.google.com', 'technology', '150k', 'Newyork','search');
