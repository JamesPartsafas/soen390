-- Password is 12345678, all security answers = "a"
INSERT INTO app_user (name, password, email, role, auth_provider, security_answer1, security_answer2, security_answer3, verification_status)
VALUES ('Joe User', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joeuser@mail.com', 'CANDIDATE',
        'LOCAL', '$2a$10$6.8QMAk88DPFHH//rEchFOZpsYqe8eJYsx0bYHQuB1QCuEp69pf1q',
        '$2a$10$6.8QMAk88DPFHH//rEchFOZpsYqe8eJYsx0bYHQuB1QCuEp69pf1q',
        '$2a$10$6.8QMAk88DPFHH//rEchFOZpsYqe8eJYsx0bYHQuB1QCuEp69pf1q', false);

INSERT INTO app_user (name, password, email, role, auth_provider, verification_status)
VALUES ('Joe Admin', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joeadmin@mail.com', 'ADMIN',
        'LOCAL', false);

INSERT INTO app_user (name, password, email, role, auth_provider, verification_status)
VALUES ('Joe Company', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joecompany@mail.com', 'COMPANY',
        'LOCAL', false);

INSERT INTO app_user (name, password, email, role, auth_provider, verification_status)
VALUES ('Joe Google', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joegoogle@mail.com', 'CANDIDATE',
        'GOOGLE', false);

INSERT INTO app_user (name, password, email, role, auth_provider, company_id, verification_status)
VALUES ('Joe Recruiter', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joerecruiter@mail.com',
        'RECRUITER', 'LOCAL', 3, false);

INSERT INTO app_user (name, password, email, role, auth_provider, company_id, verification_status)
VALUES ('Joe Recruiter2', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'joerecruiter2@mail.com',
        'RECRUITER', 'LOCAL', 5, false);

INSERT INTO app_user (name, password, email, role, auth_provider, verification_status)
VALUES ('Tony Recruiter', '$2a$12$ShsRhBqy8y9ep/oxB5ury.7cxmcGjt.BQA4i6dhp/RUva/hS7DjHm', 'tonyrecruiter@mail.com',
        'RECRUITER', 'LOCAL', false);

INSERT INTO connection (receiver_id, requester_id, pending)
VALUES (1, 2, false);

INSERT INTO connection (receiver_id, requester_id, pending)
VALUES (3, 1, false);

INSERT INTO connection (receiver_id, requester_id, pending)
VALUES (1, 4, true);
INSERT INTO app_user_profile(app_user_id, description, education, skill, work, course,
                             phone, volunteering, project, award, language)
VALUES (1, 'A standard user on Synapsis', 'Engineeing', 'fast learner', 'developer', 'ML', '514-444-4444', 'sss', 'xyz',
        'CARE', 'ARABIC');

INSERT INTO company_profile(app_user_id, description, website, industry, company_size, location, speciality)
VALUES (3, 'A standard company on Synapsis', 'www.google.com', 'technology', '150', 'Newyork', 'search');

INSERT INTO chat (creator_id, participant_id, last_updated)
VALUES (1, 5, '2022-01-01'),
       (4, 1, '2023-01-01');

INSERT INTO message (chat_id, content, sender_id, read, report_status, created_at)
VALUES (1, 'Hi from Joe User', 1, false, 'UNREPORTED', '2022-01-02'),
       (1, 'Hi from Joe Recruiter', 5, false, 'REPORTED', '2022-01-03'),
       (2, 'Hi from Joe Google', 5, false, 'UNREPORTED', '2022-01-03');

INSERT INTO job (creator_id, position, company, address, description, type, num_available, num_applicants, is_external,
                 external_link, need_resume, need_cover, need_portfolio)
VALUES (5, 'VBA Developer', 'Macrosoft', '123 King Street',
        'We are seeking an experienced VBA developer to join our team. You will be responsible for developing, maintaining, and enhancing Excel-based applications and tools that are used across the company. You will work closely with other developers, business analysts, and end-users to deliver high-quality, user-friendly solutions that meet business needs.',
        0, 5, 10, false, '', true, true, true),
       (5, 'Graphic Artist', 'Adibas', '789 Main Street',
        'Our company is seeking a creative and skilled graphic artist to join our team. As a graphic artist, you will work closely with our marketing and design teams to create visually appealing and effective graphics for our footwear. Your designs will be a key factor in promoting our brand and driving sales.',
        1, 3, 6, false, '', true, true, true),
       (5, 'Industrial Engineer', 'Isded', '1011 Popper Street',
        'Isded is seeking an experienced industrial engineer specializing in beverage packaging. You will be responsible for designing and implementing manufacturing processes to improve efficiency, productivity, and product quality. You will work closely with production teams to identify areas of improvement and develop solutions that optimize our manufacturing process.',
        1, 1, 2, false, '', true, true, true),
       (5, 'Course Content Writer', 'Craigstutors', '203 Level Street',
        'Looking for talented and creative content writers to develop educational materials such as lesson plans, assessments, worksheets, and other learning resources fo students ranging from elementary to K-12. Preferably, you should have at least 3 years of experience.',
        0, 1, 1, true, 'https://www.google.com/', true, true, true),
       (5, 'Software Developer', 'Macrosoft', '123 King Street',
        'We are looking for a Software Developer to build and implement functional programs. You will work with other Developers and Product Managers throughout the software development life cycle. In this role, you should be a team player with a keen eye for detail and problem-solving skills. If you also have experience in Agile frameworks and popular coding languages (e.g. JavaScript), we’d like to meet you.',
        0, 2, 0, false, '', true, false, false);

INSERT INTO notification (recipient_id, text, url, seen, creation_time)
VALUES (1, 'This is a notification. It brings you to messages.', '/chats', false, '2023-03-01');

INSERT INTO settings (app_user_id, job_email_notifications_on, message_email_notifications_on,
                      connection_email_notifications_on)
VALUES (1, true, true, true);