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
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al guardar el teléfono.", e);
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
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al actualizar el teléfono.", e);
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
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al eliminar el teléfono.", e);
		}		
	}

	@Override
	public Telefono findById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(Telefono.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar el teléfono por ID.", e);
		}
	}

	@Override
	public List<Telefono> findAll() {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			List<Telefono> telefonos = session.createQuery("FROM TELEFONOS", Telefono.class).getResultList();						
			transaction.commit();
			return telefonos;
		} catch (Exception e) {
			throw new DAOException("Error al obtener todos los teléfonos.", e);
		}		
	}
}
