DROP TABLE IF EXISTS restaurante CASCADE;
CREATE TABLE restaurante
(
    restaurante_nombre          VARCHAR(256),
    restaurante_cantidad_mesas  INT NOT NULL,
    PRIMARY KEY (restaurante_nombre)
);

DROP TABLE IF EXISTS mesa CASCADE;
CREATE TABLE mesa
(
    mesa_numero             INT,
    mesa_fumador            BOOLEAN         NOT NULL,
    mesa_estado             VARCHAR(128)    NOT NULL,
    mesa_restaurante_nombre VARCHAR(256)    NOT NULL,
    PRIMARY KEY (mesa_numero, mesa_restaurante_nombre),
    FOREIGN KEY mesa (mesa_restaurante) REFERENCES restaurante (restaurante_nombre)
);

