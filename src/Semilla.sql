DROP TABLE IF EXISTS restaurante CASCADE;
CREATE TABLE restaurante
(
    restaurante_nombre          VARCHAR(256),
    PRIMARY KEY (restaurante_nombre)
);

DROP TABLE IF EXISTS mesa CASCADE;
CREATE TABLE mesa
(
    mesa_numero             INT,
    mesa_capacidad          INT             NOT NULL,
    mesa_fumador            BOOLEAN         NOT NULL,
    mesa_estado             VARCHAR(128)    NOT NULL,
    mesa_restaurante_nombre VARCHAR(256)    NOT NULL,
    PRIMARY KEY (mesa_numero, mesa_restaurante_nombre),
    FOREIGN KEY mesa (mesa_restaurante_nombre) REFERENCES restaurante (restaurante_nombre)
);

DROP TABLE IF EXISTS reserva CASCADE;
CREATE TABLE reserva
(
    reserva_fecha                   VARCHAR(64),
    reserva_hora                    VARCHAR(8)      NOT NULL,
    reserva_nombre                  VARCHAR(128)    NOT NULL,
    reserva_mesa_numero             INT             NOT NULL,
    reserva_mesa_restaurante_nombre VARCHAR(256)    NOT NULL,
    PRIMARY KEY (reserva_fecha, reserva_mesa_numero, reserva_mesa_restaurante_nombre),
    FOREIGN KEY reserva (reserva_mesa_numero, reserva_mesa_restaurante_nombre) REFERENCES mesa (mesa_numero, mesa_restaurante_nombre)
);

INSERT INTO restaurante VALUES ('RESTAURANTE 1');
INSERT INTO restaurante VALUES ('RESTAURANTE 2');
INSERT INTO restaurante VALUES ('RESTAURANTE 3');
INSERT INTO restaurante VALUES ('RESTAURANTE 4');
INSERT INTO restaurante VALUES ('RESTAURANTE 5');

INSERT INTO mesa VALUES (1, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (2, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (3, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (4, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (5, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (6, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (7, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (8, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (9, TRUE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (10, TRUE, 'LIBRE', 'RESTAURANTE 1');

