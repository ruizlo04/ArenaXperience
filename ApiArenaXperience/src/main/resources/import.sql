INSERT INTO user_entity (id, username, password, email, phone_number, created_at, enabled, activation_token)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', '{noop}admin123', 'admin@example.com', '123456789', NOW(), true, NULL);

INSERT INTO usuario_roles (usuario_id, roles)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'ADMIN');

INSERT INTO event_entity (id, name, date, capacity, admin_id)
VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Barcelona-Real Madrid', '2025-07-20', 750, '550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440002', 'Valencia-Atlético Madrid', '2025-08-10', 600, '550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440003', 'Sevilla-Villarreal', '2025-09-05', 700, '550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440004', 'Real Sociedad-Athletic Bilbao', '2025-10-12', 650, '550e8400-e29b-41d4-a716-446655440000'),
('550e8400-e29b-41d4-a716-446655440005', 'Celta de Vigo-Granada', '2025-11-22', 600, '550e8400-e29b-41d4-a716-446655440000');

