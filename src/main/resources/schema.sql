CREATE TABLE CLIENTES (
    id_tx VARCHAR(36) PRIMARY KEY,
    tipo_documento VARCHAR(10) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,
    primer_nombre VARCHAR(50) NOT NULL,
    segundo_nombre VARCHAR(50),
    primer_apellido VARCHAR(50) NOT NULL,
    segundo_apellido VARCHAR(50),
    telefono BIGINT NOT NULL,
    corre_electronico VARCHAR(100) NOT NULL
);
