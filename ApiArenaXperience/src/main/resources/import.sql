INSERT INTO user_entity (id, username, password, email, phone_number, created_at, enabled, activation_token)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', '{noop}admin123', 'admin@example.com', '123456789', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440006', 'user1', '{noop}user123', 'user1@example.com', '987654321', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440007', 'user2', '{noop}user124', 'user2@example.com', '654321987', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440008', 'user3', '{noop}user125', 'user3@example.com', '654321777', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440009', 'user4', '{noop}user126', 'user4@example.com', '654321779', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440010', 'user5', '{noop}user127', 'user5@example.com', '654321780', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440011', 'user6', '{noop}user128', 'user6@example.com', '654321781', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440012', 'user7', '{noop}user129', 'user7@example.com', '654321782', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440013', 'user8', '{noop}user130', 'user8@example.com', '654321783', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440014', 'user9', '{noop}user131', 'user9@example.com', '654321784', NOW(), true, NULL),
      ('550e8400-e29b-41d4-a716-446655440015', 'user10', '{noop}user132', 'user10@example.com', '654321785', NOW(), true, NULL);


INSERT INTO usuario_roles (usuario_id, roles)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'ADMIN'),
      ('550e8400-e29b-41d4-a716-446655440006', 'USER'),
      ('550e8400-e29b-41d4-a716-446655440007', 'USER'),
      ('550e8400-e29b-41d4-a716-446655440008', 'SOCIO'),
      ('550e8400-e29b-41d4-a716-446655440009', 'SOCIO'),
      ('550e8400-e29b-41d4-a716-446655440010', 'SOCIO'),
      ('550e8400-e29b-41d4-a716-446655440011', 'USER'),
      ('550e8400-e29b-41d4-a716-446655440012', 'USER'),
      ('550e8400-e29b-41d4-a716-446655440013', 'SOCIO'),
      ('550e8400-e29b-41d4-a716-446655440014', 'SOCIO'),
      ('550e8400-e29b-41d4-a716-446655440015', 'SOCIO');


INSERT INTO event_entity (id, name, date, capacity, price, precio_total_recaudado, admin_id, file)
VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Barcelona-Real Madrid', '2025-07-20', 750, 20.0, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Forcejeo_Real_Madrid_-_FC_Barcelona.jpg/1200px-Forcejeo_Real_Madrid_-_FC_Barcelona.jpg'),
('550e8400-e29b-41d4-a716-446655440002', 'Valencia-Atlético Madrid', '2025-08-10', 600, 19.5, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://cdn.vox-cdn.com/thumbor/BLgfFZrBzjGEgQY-bk0-GxnQMsc=/0x0:4000x2667/1200x800/filters:focal(2144x883:2784x1523)/cdn.vox-cdn.com/uploads/chorus_image/image/65507429/1182072956.jpg.0.jpg'),
('550e8400-e29b-41d4-a716-446655440003', 'Sevilla-Villarreal', '2025-09-05', 700, 15.0, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://s2.abcstatics.com/abc/www/multimedia/deportes/2025/05/18/sevilla-villarreal-doblado-U61031631141mgI-1024x512@diario_abc.JPG'),
('550e8400-e29b-41d4-a716-446655440004', 'Real Sociedad-Athletic Bilbao', '2025-10-12', 650, 20.0, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://cdn.athletic-club.eus/uploads/2023/09/WhatsApp%20Image%202023-09-30%20at%2021.12.45%20(2).jpg'),
('550e8400-e29b-41d4-a716-446655440005', 'Celta de Vigo-Granada', '2025-11-22', 600, 16.5, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://www.granadadigital.es/wp-content/uploads/2024/05/Foto-Antonio-L-Juarez-Granada-CF-Celta-de-Vigo-37-1010x673.jpg'),
('550e8400-e29b-41d4-a716-446655440016', 'Getafe-Betis', '2025-12-01', 500, 18.0, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://image-service.onefootball.com/transform?w=280&h=210&dpr=2&image=https%3A%2F%2Fagentelibredigital.com%2Fwp-content%2Fuploads%2F2025%2F02%2Fgetafe-cf-v-real-betis-balompie-la-liga-ea-sports-scaled.jpg'),
('550e8400-e29b-41d4-a716-446655440017', 'Alavés-Espanyol', '2025-12-15', 550, 17.5, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://www.mundodeportivo.com/files/image_449_465/files/fp/uploads/2025/02/22/67b9f2d3a3541.r_d.2495-1381-898.jpeg'),
('550e8400-e29b-41d4-a716-446655440018', 'Mallorca-Cádiz', '2026-01-10', 600, 16.0, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://estaticos-cdn.prensaiberica.es/clip/3d6bcca2-924e-4fa1-aee9-1941d0baf2c7_source-aspect-ratio_default_0.jpg'),
('550e8400-e29b-41d4-a716-446655440019', 'Osasuna-Rayo Vallecano', '2026-02-05', 650, 19.0, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://e00-marca.uecdn.es/assets/multimedia/imagenes/2023/04/14/16815053250571.jpg'),
('550e8400-e29b-41d4-a716-446655440020', 'Elche-Levante', '2026-03-01', 700, 20.5, 0, '550e8400-e29b-41d4-a716-446655440000', 'https://imgresizer.eurosport.com/unsafe/1200x0/filters:format(jpeg)/origin-imgresizer.eurosport.com/2021/09/18/3222614-65969548-2560-1440.jpg');


INSERT INTO review_entity (id, comment, rating, event_id, user_id)
VALUES
   ('550e8400-e29b-41d4-a716-446655440101', '¡Gran partido! Muy emocionante.', 5, '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440008'),
   ('550e8400-e29b-41d4-a716-446655440102', 'El ambiente fue increíble, pero el precio un poco alto.', 4, '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440008'),
   ('550e8400-e29b-41d4-a716-446655440103', 'Buena organización, pero el partido fue aburrido.', 3, '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440008'),
   ('550e8400-e29b-41d4-a716-446655440104', 'Increíble experiencia, volvería sin duda.', 5, '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440008'),
   ('550e8400-e29b-41d4-a716-446655440105', 'El partido estuvo bien, pero la comida en el estadio fue cara.', 4, '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440008'),
   ('550e8400-e29b-41d4-a716-446655440106', 'El partido estuvo muy igualado, buen espectáculo.', 4, '550e8400-e29b-41d4-a716-446655440016', '550e8400-e29b-41d4-a716-446655440013'),
   ('550e8400-e29b-41d4-a716-446655440107', 'El estadio es pequeño pero acogedor.', 3, '550e8400-e29b-41d4-a716-446655440017', '550e8400-e29b-41d4-a716-446655440014'),
   ('550e8400-e29b-41d4-a716-446655440108', 'Muy buen ambiente, pero el tráfico para llegar fue horrible.', 4, '550e8400-e29b-41d4-a716-446655440018', '550e8400-e29b-41d4-a716-446655440015'),
   ('550e8400-e29b-41d4-a716-446655440109', 'El partido fue emocionante, pero la organización dejó que desear.', 3, '550e8400-e29b-41d4-a716-446655440019', '550e8400-e29b-41d4-a716-446655440013'),
   ('550e8400-e29b-41d4-a716-446655440110', 'Increíble experiencia, el equipo local jugó muy bien.', 5, '550e8400-e29b-41d4-a716-446655440020', '550e8400-e29b-41d4-a716-446655440014');


INSERT INTO ticket_entity (id, event_id, user_id, cantidad, precio_final)
VALUES
('550e8400-e29b-41d4-a716-446655440201', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440006', 2, 40.0),
('550e8400-e29b-41d4-a716-446655440202', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440007', 1, 20.0),
('550e8400-e29b-41d4-a716-446655440203', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440008', 3, 60.0),
('550e8400-e29b-41d4-a716-446655440204', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440006', 1, 19.5),
('550e8400-e29b-41d4-a716-446655440205', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440007', 2, 39.0),
('550e8400-e29b-41d4-a716-446655440206', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440006', 1, 15.0),
('550e8400-e29b-41d4-a716-446655440207', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440008', 2, 30.0),
('550e8400-e29b-41d4-a716-446655440208', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440007', 1, 20.0),
('550e8400-e29b-41d4-a716-446655440209', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440008', 2, 40.0),
('550e8400-e29b-41d4-a716-446655440210', '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440006', 2, 33.0),
('550e8400-e29b-41d4-a716-446655440211', '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440007', 2, 33.0),
('550e8400-e29b-41d4-a716-446655440212', '550e8400-e29b-41d4-a716-446655440016', '550e8400-e29b-41d4-a716-446655440011', 1, 18.0),
('550e8400-e29b-41d4-a716-446655440213', '550e8400-e29b-41d4-a716-446655440016', '550e8400-e29b-41d4-a716-446655440012', 1, 18.0),
('550e8400-e29b-41d4-a716-446655440214', '550e8400-e29b-41d4-a716-446655440017', '550e8400-e29b-41d4-a716-446655440011', 1, 17.5),
('550e8400-e29b-41d4-a716-446655440215', '550e8400-e29b-41d4-a716-446655440018', '550e8400-e29b-41d4-a716-446655440012', 2, 32.0),
('550e8400-e29b-41d4-a716-446655440216', '550e8400-e29b-41d4-a716-446655440019', '550e8400-e29b-41d4-a716-446655440011', 1, 19.0),
('550e8400-e29b-41d4-a716-446655440217', '550e8400-e29b-41d4-a716-446655440020', '550e8400-e29b-41d4-a716-446655440012', 3, 61.5);


INSERT INTO chat_entity (id, sender_id, receiver_id, message, sent_at)
VALUES
   ('550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440007', 'Hola, ¿cómo estás?', NOW()),
   ('550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440006', '¡Hola! Estoy bien, ¿y tú?', NOW()),
   ('550e8400-e29b-41d4-a716-446655440303', '550e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440012', '¿Vas al partido de Getafe?', NOW()),
   ('550e8400-e29b-41d4-a716-446655440304', '550e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440011', 'Sí, ya tengo mi entrada. ¿Y tú?', NOW()),
   ('550e8400-e29b-41d4-a716-446655440305', '550e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440014', '¿Qué te pareció el partido de ayer?', NOW()),
   ('550e8400-e29b-41d4-a716-446655440306', '550e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440013', 'Fue emocionante, aunque el árbitro tuvo algunas decisiones cuestionables.', NOW());
