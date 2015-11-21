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
    FOREIGN KEY (mesa_restaurante_nombre) REFERENCES restaurante (restaurante_nombre)
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
    FOREIGN KEY (reserva_mesa_numero, reserva_mesa_restaurante_nombre) REFERENCES mesa (mesa_numero, mesa_restaurante_nombre)
);

DROP TABLE IF EXISTS ingrediente CASCADE;
CREATE TABLE ingrediente
(
    ingrediente_id                  VARCHAR(50),
    ingrediente_nombre              VARCHAR(50)     NOT NULL,
    ingrediente_cantidad            INT             NOT NULL,
    ingrediente_restaurante_nombre  VARCHAR(256)    NOT NULL,
    PRIMARY KEY (ingrediente_id, ingrediente_nombre),
    FOREIGN KEY (ingrediente_restaurante_nombre) REFERENCES restaurante(restaurante_nombre)
);

INSERT INTO restaurante VALUES ('RESTAURANTE 1');
INSERT INTO restaurante VALUES ('RESTAURANTE 2');
INSERT INTO restaurante VALUES ('RESTAURANTE 3');
INSERT INTO restaurante VALUES ('RESTAURANTE 4');
INSERT INTO restaurante VALUES ('RESTAURANTE 5');

INSERT INTO mesa VALUES (1, 5, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (2, 5, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (3, 5, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (4, 10, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (5, 10, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (6, 10, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (7, 15, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (8, 15, FALSE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (9, 15, TRUE, 'LIBRE', 'RESTAURANTE 1');
INSERT INTO mesa VALUES (10, 20, TRUE, 'LIBRE', 'RESTAURANTE 1');

INSERT INTO ingrediente VALUES ('1', 'TOMATE', 20, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('2', 'LECHUGA', 10, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('3', 'CEBOLLA', 15, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('4', 'CARNE DE RES', 50, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('5', 'CARNE DE CERDO', 30, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('6', 'ACEITE', 80, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('7', 'SAL', 40, 'RESTAURANTE 1');