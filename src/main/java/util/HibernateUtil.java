package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import model.*;

public class HibernateUtil {
    private static SessionFactory factory;

    static {
        try {
        	executeSQL();
        	
            factory = new Configuration()
                      .configure("hibernate.cfg.xml")
                      .addAnnotatedClass(Cliente.class)
                      .addAnnotatedClass(Dispositivo.class)
                      .addAnnotatedClass(Componente.class)
                      .addAnnotatedClass(Estado.class)
                      .addAnnotatedClass(Reparacion.class)
                      .addAnnotatedClass(ComponenteReparacion.class)
                      .addAnnotatedClass(Telefono.class)
                      .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("SessionFactory creation failed." + e.getMessage());
        }
    }
    
    public static void executeSQL() throws Exception {
        String sql = """
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
				    tipo TEXT NOT NULL,
				    fabricante TEXT NOT NULL,
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
        		""";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:vulcano-lite.db");
             Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON;");

            for (String stmt : sql.split(";")) {
                String trimmed = stmt.trim();
                if (!trimmed.isEmpty()) {
                    statement.executeUpdate(trimmed);
                }
            }
        }
    }

    public static SessionFactory getSessionFactory() {
        return factory;
    }
}
