DROP TABLE IF EXISTS restaurante CASCADE;
CREATE TABLE restaurante
(
    restaurante_nombre          VARCHAR(256)    NOT NULL,
    PRIMARY KEY (restaurante_nombre)
);

DROP TABLE IF EXISTS mesa CASCADE;
CREATE TABLE mesa
(
    mesa_numero             INT             NOT NULL,
    mesa_capacidad          INT             NOT NULL,
    mesa_fumador            BOOLEAN         NOT NULL,
    mesa_estado             VARCHAR(128)    NOT NULL,
    restaurante_nombre      VARCHAR(256)    NOT NULL,
    PRIMARY KEY (mesa_numero, restaurante_nombre),
    FOREIGN KEY (restaurante_nombre) REFERENCES restaurante (restaurante_nombre)
);

DROP TABLE IF EXISTS reserva CASCADE;
CREATE TABLE reserva
(
    reserva_fecha                   VARCHAR(64)     NOT NULL,
    reserva_hora                    VARCHAR(8)      NOT NULL,
    reserva_nombre                  VARCHAR(128)    NOT NULL,
    reserva_numero_personas         INT             NOT NULL,
    mesa_numero                     INT             NOT NULL,
    restaurante_nombre              VARCHAR(256)    NOT NULL,
    PRIMARY KEY (reserva_fecha, mesa_numero, restaurante_nombre),
    FOREIGN KEY (mesa_numero, restaurante_nombre) REFERENCES mesa (mesa_numero, restaurante_nombre)
);

DROP TABLE IF EXISTS consumicion CASCADE;
CREATE TABLE consumicion
(
    consumicion_id                  VARCHAR(32)     NOT NULL,
    consumicion_nombre              VARCHAR(128)    NOT NULL,
    restaurante_nombre              VARCHAR(256)    NOT NULL,
    PRIMARY KEY (consumicion_id, restaurante_nombre),
    FOREIGN KEY (restaurante_nombre) REFERENCES restaurante (restaurante_nombre)
);

DROP TABLE IF EXISTS ingrediente CASCADE;
CREATE TABLE ingrediente
(
    ingrediente_id                  VARCHAR(32)     NOT NULL,
    ingrediente_nombre              VARCHAR(128)    NOT NULL,
    ingrediente_cantidad            INT             NOT NULL,
    restaurante_nombre              VARCHAR(256)    NOT NULL,
    PRIMARY KEY (ingrediente_id, restaurante_nombre),
    FOREIGN KEY (restaurante_nombre) REFERENCES restaurante (restaurante_nombre)
);

DROP TABLE IF EXISTS pedido CASCADE;
CREATE TABLE pedido
(
    pedido_fecha                    VARCHAR(32)     NOT NULL,
    pedido_estado                   VARCHAR(32)     NOT NULL,
    mesa_numero                     INT             NOT NULL,
    restaurante_nombre              VARCHAR(256)    NOT NULL,
    PRIMARY KEY (pedido_fecha, mesa_numero, restaurante_nombre),
    FOREIGN KEY (mesa_numero, restaurante_nombre) REFERENCES mesa (mesa_numero, restaurante_nombre)
);

DROP TABLE IF EXISTS consumicion_ingrediente CASCADE;
CREATE TABLE consumicion_ingrediente
(
    consumicion_ingrediente_cantidad    INT             NOT NULL,
    consumicion_id                      VARCHAR(32)     NOT NULL,
    ingrediente_id                      VARCHAR(32)     NOT NULL,
    restaurante_nombre                  VARCHAR(256)    NOT NULL,
    PRIMARY KEY (consumicion_id, ingrediente_id, restaurante_nombre),
    FOREIGN KEY (consumicion_id, restaurante_nombre) REFERENCES consumicion (consumicion_id, restaurante_nombre),
    FOREIGN KEY (ingrediente_id, restaurante_nombre) REFERENCES ingrediente (ingrediente_id, restaurante_nombre)
);

DROP TABLE IF EXISTS pedido_consumicion CASCADE;
CREATE TABLE pedido_consumicion
(
    pedido_consumicion_id           SERIAL          NOT NULL,
    pedido_consumicion_estado       VARCHAR(32)     NOT NULL,
    pedido_fecha                    VARCHAR(32)     NOT NULL,
    mesa_numero                     INT             NOT NULL,
    restaurante_nombre              VARCHAR(256)    NOT NULL,
    consumicion_id                  VARCHAR(32)     NOT NULL,
    PRIMARY KEY (pedido_consumicion_id),
    FOREIGN KEY (pedido_fecha, mesa_numero, restaurante_nombre) REFERENCES pedido (pedido_fecha, mesa_numero, restaurante_nombre),
    FOREIGN KEY (consumicion_id, restaurante_nombre) REFERENCES consumicion (consumicion_id, restaurante_nombre)
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

INSERT INTO reserva VALUES ('21/11/2015', '1200', 'Pepito', 10, 2, 'RESTAURANTE 1');
INSERT INTO reserva VALUES ('21/11/2015', '1200', 'Pepito', 10, 4, 'RESTAURANTE 1');
INSERT INTO reserva VALUES ('21/11/2015', '1200', 'Pepito', 10, 7, 'RESTAURANTE 1');
INSERT INTO reserva VALUES ('21/11/2015', '1200', 'Pepito', 10, 1, 'RESTAURANTE 1');
INSERT INTO reserva VALUES ('21/11/2015', '1200', 'Pepito', 10, 10, 'RESTAURANTE 1');
INSERT INTO reserva VALUES ('20/11/2015', '1200', 'Pepito', 10, 3, 'RESTAURANTE 1');
INSERT INTO reserva VALUES ('20/11/2015', '1200', 'Pepito', 10, 8, 'RESTAURANTE 1');
INSERT INTO reserva VALUES ('20/11/2015', '1200', 'Pepito', 10, 6, 'RESTAURANTE 1');

INSERT INTO consumicion VALUES ('1000', 'HAMBURGUESA RES', 'RESTAURANTE 1');
INSERT INTO consumicion VALUES ('1001', 'HAMBURGUESA CERDO', 'RESTAURANTE 1');
INSERT INTO consumicion VALUES ('1002', 'CARNE ASADA', 'RESTAURANTE 1');
INSERT INTO consumicion VALUES ('1003', 'CERDO ASADO', 'RESTAURANTE 1');
INSERT INTO consumicion VALUES ('1004', 'ENSALADA', 'RESTAURANTE 1');

INSERT INTO ingrediente VALUES ('1', 'TOMATE', 20, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('2', 'LECHUGA', 10, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('3', 'CEBOLLA', 15, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('4', 'CARNE DE RES', 0, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('5', 'CARNE DE CERDO', 30, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('6', 'ACEITE', 80, 'RESTAURANTE 1');
INSERT INTO ingrediente VALUES ('7', 'SAL', 40, 'RESTAURANTE 1');

INSERT INTO consumicion_ingrediente VALUES (1, '1000', '1', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1000', '2', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1000', '3', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1000', '4', 'RESTAURANTE 1');

INSERT INTO consumicion_ingrediente VALUES (1, '1001', '1', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1001', '2', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1001', '3', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1001', '5', 'RESTAURANTE 1');

INSERT INTO consumicion_ingrediente VALUES (1, '1002', '4', 'RESTAURANTE 1');

INSERT INTO consumicion_ingrediente VALUES (1, '1003', '5', 'RESTAURANTE 1');

INSERT INTO consumicion_ingrediente VALUES (1, '1004', '1', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1004', '2', 'RESTAURANTE 1');
INSERT INTO consumicion_ingrediente VALUES (1, '1004', '3', 'RESTAURANTE 1');
