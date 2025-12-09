INSERT IGNORE INTO regions (id, code, name)
VALUES (1, '01', 'ANDALUCÍA'),
       (2, '02', 'ARAGÓN'),
       (3, '03', 'ASTURIAS'),
       (4, '04', 'BALEARES'),
       (5, '05', 'CANARIAS'),
       (6, '06', 'CANTABRIA'),
       (7, '07', 'CASTILLA Y LEÓN'),
       (8, '08', 'CASTILLA-LA MANCHA'),
       (9, '09', 'CATALUÑA'),
       (10, '10', 'COMUNIDAD VALENCIANA'),
       (11, '11', 'EXTREMADURA'),
       (12, '12', 'GALICIA'),
       (13, '13', 'MADRID'),
       (14, '14', 'MURCIA'),
       (15, '15', 'NAVARRA'),
       (16, '16', 'PAÍS VASCO'),
       (17, '17', 'LA RIOJA'),
       (18, '18', 'CEUTA Y MELILLA');

INSERT IGNORE INTO provinces (id, code, name, region_id)
VALUES
-- ANDALUCÍA (1)
(1, '01', 'Almería', 1),
(2, '02', 'Cádiz', 1),
(3, '03', 'Córdoba', 1),
(4, '04', 'Granada', 1),
(5, '05', 'Huelva', 1),
(6, '06', 'Jaén', 1),
(7, '07', 'Málaga', 1),
(8, '08', 'Sevilla', 1),

-- ARAGÓN (2)
(9, '09', 'Huesca', 2),
(10, '10', 'Teruel', 2),
(11, '11', 'Zaragoza', 2),

-- ASTURIAS (3)
(12, '12', 'Asturias', 3),

-- BALEARES (4)
(13, '13', 'Islas Baleares', 4),

-- CANARIAS (5)
(14, '14', 'Las Palmas', 5),
(15, '15', 'Santa Cruz de Tenerife', 5),

-- CANTABRIA (6)
(16, '16', 'Cantabria', 6),

-- CASTILLA Y LEÓN (7)
(17, '17', 'Ávila', 7),
(18, '18', 'Burgos', 7),
(19, '19', 'León', 7),
(20, '20', 'Palencia', 7),
(21, '21', 'Salamanca', 7),
(22, '22', 'Segovia', 7),
(23, '23', 'Soria', 7),
(24, '24', 'Valladolid', 7),
(25, '25', 'Zamora', 7),

-- CASTILLA-LA MANCHA (8)
(26, '26', 'Albacete', 8),
(27, '27', 'Ciudad Real', 8),
(28, '28', 'Cuenca', 8),
(29, '29', 'Guadalajara', 8),
(30, '30', 'Toledo', 8),

-- CATALUÑA (9)
(31, '31', 'Barcelona', 9),
(32, '32', 'Girona', 9),
(33, '33', 'Lleida', 9),
(34, '34', 'Tarragona', 9),

-- COMUNIDAD VALENCIANA (10)
(35, '35', 'Alicante', 10),
(36, '36', 'Castellón', 10),
(37, '37', 'Valencia', 10),

-- EXTREMADURA (11)
(38, '38', 'Badajoz', 11),
(39, '39', 'Cáceres', 11),

-- GALICIA (12)
(40, '40', 'A Coruña', 12),
(41, '41', 'Lugo', 12),
(42, '42', 'Ourense', 12),
(43, '43', 'Pontevedra', 12),

-- MADRID (13)
(44, '44', 'Madrid', 13),

-- MURCIA (14)
(45, '45', 'Murcia', 14),

-- NAVARRA (15)
(46, '46', 'Navarra', 15),

-- PAÍS VASCO (16)
(47, '47', 'Álava', 16),
(48, '48', 'Gipuzkoa', 16),
(49, '49', 'Bizkaia', 16),

-- LA RIOJA (17)
(50, '50', 'La Rioja', 17),

-- CEUTA Y MELILLA (18)
(51, '51', 'Ceuta', 18),
(52, '52', 'Melilla', 18);