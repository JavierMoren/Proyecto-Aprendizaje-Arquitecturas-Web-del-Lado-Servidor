-- Tabla para las Comunidades Autónomas de España
CREATE TABLE IF NOT EXISTS regions (
   id INT AUTO_INCREMENT PRIMARY KEY,
   code VARCHAR(10) NOT NULL UNIQUE,
   name VARCHAR(100) NOT NULL
);


-- Inserts de las Comunidades Autónomas, ignora si se produce un error en la insercción
INSERT IGNORE INTO regions (code, name) VALUES
('01', 'ANDALUCÍA'),
('02', 'ARAGÓN'),
('03', 'ASTURIAS'),
('04', 'BALEARES'),
('05', 'CANARIAS'),
('06', 'CANTABRIA'),
('07', 'CASTILLA Y LEÓN'),
('08', 'CASTILLA-LA MANCHA'),
('09', 'CATALUÑA'),
('10', 'COMUNIDAD VALENCIANA'),
('11', 'EXTREMADURA'),
('12', 'GALICIA'),
('13', 'MADRID'),
('14', 'MURCIA'),
('15', 'NAVARRA'),
('16', 'PAÍS VASCO'),
('17', 'LA RIOJA'),
('18', 'CEUTA Y MELILLA');
