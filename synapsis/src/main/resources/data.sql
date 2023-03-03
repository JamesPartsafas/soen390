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

INSERT INTO app_user (name, password, email, role, auth_provider)
VALUES ('Tony Recruiter', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'tonyrecruiter@mail.com',
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

INSERT INTO chat (creator_id, participant_id, last_updated)
VALUES (1, 5, '2022-01-01'), (4, 1, '2023-01-01');

INSERT INTO message (chat_id, content, sender_id, read, created_at)
VALUES (1, 'Hi from Joe User', 1, false, '2022-01-02'), (1, 'Hi from Joe Recruiter', 5, false, '2022-01-03');

INSERT INTO job (creator_id, position, company, address, description, type, num_available, num_applicants)
VALUES (5, 'VBA Developer', 'Macrosoft', '123 King Street', 'We are seeking an experienced VBA developer to join our team. You will be responsible for developing, maintaining, and enhancing Excel-based applications and tools that are used across the company. You will work closely with other developers, business analysts, and end-users to deliver high-quality, user-friendly solutions that meet business needs.', 1, 5, 10),
       (5, 'Graphic Artist', 'Adibas', '789 Main Street', 'Our company is seeking a creative and skilled graphic artist to join our team. As a graphic artist, you will work closely with our marketing and design teams to create visually appealing and effective graphics for our footwear. Your designs will be a key factor in promoting our brand and driving sales.', 1, 3, 6),
       (5, 'Industrial Engineer', 'Isded', '1011 Popper Street', 'Isded is seeking an experienced industrial engineer specializing in beverage packaging. You will be responsible for designing and implementing manufacturing processes to improve efficiency, productivity, and product quality. You will work closely with production teams to identify areas of improvement and develop solutions that optimize our manufacturing process.', 1, 1, 2),
       (5, 'Course Content Writer', 'Craigstutors', '203 Level Street', 'Looking for talented and creative content writers to develop educational materials such as lesson plans, assessments, worksheets, and other learning resources fo students ranging from elementary to K-12. Preferably, you should have at least 3 years of experience.', 1, 1, 1);
