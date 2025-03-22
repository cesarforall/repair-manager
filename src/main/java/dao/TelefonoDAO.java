package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Telefono;
import util.HibernateUtil;

public class TelefonoDAO implements GenericDAO<Telefono>{
	@Override
	public void save(Telefono telefono) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			session.persist(telefono);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al guardar el teléfono.");
			e.printStackTrace();
		}		
	}

	@Override
	public void update(Telefono telefono) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(telefono);
			transaction.commit();			
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al actualizar el teléfono.");
            e.printStackTrace();
		}		
	}

	@Override
	public void delete(Telefono telefono) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(telefono);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al eliminar el teléfono.");
			e.printStackTrace();
		}		
	}

	@Override
	public Telefono findById(int id) {
		Telefono telefono = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			telefono = session.get(Telefono.class, id);			
		} catch (Exception e) {
			System.err.println("Error al buscar el teléfono por ID.");
            e.printStackTrace();
		}
		return telefono;
	}

	@Override
	public List<Telefono> findAll() {
		List<Telefono> telefonos = null;
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			telefonos = session.createQuery("FROM TELEFONOS", Telefono.class).getResultList();						
			transaction.commit();
		} catch (Exception e) {
			System.err.println("Error al obtener todos los teléfonos.");
            e.printStackTrace();
		}		
		return telefonos;
	}
}
