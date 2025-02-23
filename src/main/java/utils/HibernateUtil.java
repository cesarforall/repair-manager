package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Cliente;
import models.Dispositivo;
import models.Estado;
import models.Repuesto;

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
                      .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("SessionFactory creation failed." + e.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return factory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
