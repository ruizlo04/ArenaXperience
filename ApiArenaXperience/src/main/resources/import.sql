INSERT INTO user_entity (id, username, password, email, phone_number, created_at, enabled, activation_token)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', '{noop}admin123', 'admin@example.com', '123456789', NOW(), true, NULL),
       ('550e8400-e29b-41d4-a716-446655440006', 'user1', '{noop}user123', 'user1@example.com', '987654321', NOW(), true, NULL),
       ('550e8400-e29b-41d4-a716-446655440007', 'user2', '{noop}user124', 'user2@example.com', '654321987', NOW(), true, NULL),
       ('550e8400-e29b-41d4-a716-446655440008', 'user3', '{noop}user125', 'user3@example.com', '654321777', NOW(), true, NULL);


INSERT INTO usuario_roles (usuario_id, roles)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'ADMIN'),
       ('550e8400-e29b-41d4-a716-446655440006', 'USER'),
       ('550e8400-e29b-41d4-a716-446655440007', 'USER'),
       ('550e8400-e29b-41d4-a716-446655440008', 'SOCIO');

INSERT INTO event_entity (id, name, date, capacity, price, admin_id)
VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Barcelona-Real Madrid', '2025-07-20', 750, 20.0, '550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440002', 'Valencia-Atlético Madrid', '2025-08-10', 600, 19.5,'550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440003', 'Sevilla-Villarreal', '2025-09-05', 700, 15.0,'550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440004', 'Real Sociedad-Athletic Bilbao', '2025-10-12', 650, 20.0,'550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440005', 'Celta de Vigo-Granada', '2025-11-22', 600, 16.5,'550e8400-e29b-41d4-a716-446655440000');

