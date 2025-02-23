CREATE TABLE CLIENTES (
    id_cliente INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    detalle TEXT
);

CREATE TABLE TELEFONOS (
    id_telefono INTEGER PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    telefono TEXT NOT NULL UNIQUE,
    FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente) ON DELETE CASCADE
);

CREATE TABLE DISPOSITIVOS (
    id_dispositivo INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    modelo TEXT NOT NULL,
    n_serie TEXT UNIQUE NOT NULL
);

CREATE TABLE ESTADOS (
    id_estado INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE REPARACIONES (
    id_reparacion INTEGER PRIMARY KEY,
    id_dispositivo INTEGER NOT NULL,
    id_cliente INTEGER NOT NULL,
    id_estado INTEGER NOT NULL,
    detalle TEXT,
    f_entrada TEXT NOT NULL,
    f_salida TEXT, 
    enlace_doc TEXT,
    precio REAL NOT NULL,
    FOREIGN KEY (id_dispositivo) REFERENCES DISPOSITIVOS(id_dispositivo) ON DELETE CASCADE,
    FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_estado) REFERENCES ESTADOS(id_estado) ON DELETE SET NULL
);

CREATE TABLE REPUESTOS (
    id_repuesto INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    precio REAL NOT NULL
);

CREATE TABLE REPUESTOS_REPARACIONES (
    id_reparacion INTEGER NOT NULL,
    id_repuesto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0), 
    PRIMARY KEY (id_reparacion, id_repuesto),
    FOREIGN KEY (id_reparacion) REFERENCES REPARACIONES(id_reparacion) ON DELETE CASCADE,
    FOREIGN KEY (id_repuesto) REFERENCES REPUESTOS(id_repuesto) ON DELETE CASCADE
);
