INSERT INTO user_entity (id, username, password, email, phone_number, created_at, enabled, activation_token)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', '{noop}admin123', 'admin@example.com', '123456789', NOW(), true, NULL);

INSERT INTO usuario_roles (usuario_id, roles)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'ADMIN');
