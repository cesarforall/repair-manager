import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import models.*;
import utils.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.getCurrentSession();

        try {
        	session.beginTransaction();
        	
            Cliente cliente = session.get(Cliente.class, 1);

            if (cliente != null) {
                System.out.println("Conexión exitosa! Cliente encontrado: " + cliente.getNombre());
            } else {
                System.out.println("No se encontró el cliente con el ID 1.");
            }
        } catch (HibernateException e) {
            System.err.println("Error durante la conexión o la transacción: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }
    }
}
