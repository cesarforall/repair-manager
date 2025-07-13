CREATE TABLE IF NOT EXISTS CLIENTES (
    id_cliente INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    detalle TEXT
);

CREATE TABLE IF NOT EXISTS TELEFONOS (
    id_telefono INTEGER PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    telefono TEXT NOT NULL UNIQUE,
    FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS DISPOSITIVOS (
    id_dispositivo INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    modelo TEXT NOT NULL,
    n_serie TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS ESTADOS (
    id_estado INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS REPARACIONES (
    id_reparacion INTEGER PRIMARY KEY,
    id_dispositivo INTEGER NOT NULL,
    id_cliente INTEGER NOT NULL,
    id_estado INTEGER NOT NULL,
    detalle TEXT,
    f_entrada TEXT NOT NULL,
    f_salida TEXT, 
    enlace_doc TEXT,
    ingresos REAL NOT NULL,
    gastos REAL NOT NULL,
    FOREIGN KEY (id_dispositivo) REFERENCES DISPOSITIVOS(id_dispositivo) ON DELETE RESTRICT,
    FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente) ON DELETE RESTRICT,
    FOREIGN KEY (id_estado) REFERENCES ESTADOS(id_estado) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS COMPONENTES (
    id_componente INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    precio REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS COMPONENTES_REPARACIONES (
    id_reparacion INTEGER NOT NULL,
    id_componente INTEGER NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0), 
    PRIMARY KEY (id_reparacion, id_componente),
    FOREIGN KEY (id_reparacion) REFERENCES REPARACIONES(id_reparacion) ON DELETE RESTRICT,
    FOREIGN KEY (id_componente) REFERENCES COMPONENTES(id_componente) ON DELETE RESTRICT
);
