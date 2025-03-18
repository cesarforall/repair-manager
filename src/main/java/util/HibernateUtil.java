package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import model.*;

public class HibernateUtil {
    private static SessionFactory factory;

    static {
        try {
            factory = new Configuration()
                      .configure("hibernate.cfg.xml")
                      .addAnnotatedClass(Cliente.class)
                      .addAnnotatedClass(Dispositivo.class)
                      .addAnnotatedClass(Repuesto.class)
                      .addAnnotatedClass(Estado.class)
                      .addAnnotatedClass(Reparacion.class)
                      .addAnnotatedClass(RepuestoReparacion.class)
                      .addAnnotatedClass(Telefono.class)
                      .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("SessionFactory creation failed." + e.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return factory;
    }
}
