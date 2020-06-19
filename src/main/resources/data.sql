insert into roles(id, created_at, deleted_at, updated_at, description, role)
values (nextval('s_roles'), now(), null, null, 'Администратор', 'ROLE_ADMIN');

insert into roles(id, created_at, deleted_at, updated_at, description, role)
values (nextval('s_roles'), now(), null, null, 'Ментор', 'ROLE_TEACHER');

insert into roles(id, created_at, deleted_at, updated_at, description, role)
values (nextval('s_roles'), now(), null, null, 'Ученик', 'ROLE_USER');

insert into users (id, created_at, deleted_at, updated_at, email, first_name, last_name, password)
values (nextval('s_users'), now(), null, null, 'admin@bitlab.kz', 'Ильяс', 'Жуанышев', '$2y$12$n9J05nMik13/0m8uLva9puGss4B2IosvrAuhfDMwNaPJzkVRz9FnO
');

insert into user_roles (user_id, role_id) values (1, 1);
insert into user_roles (user_id, role_id) values (1, 3);