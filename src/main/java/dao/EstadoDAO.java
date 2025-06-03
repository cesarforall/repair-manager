package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Estado;
import util.HibernateUtil;

public class EstadoDAO implements GenericDAO<Estado>{
	@Override
	public void save(Estado estado) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			session.persist(estado);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al guardar el estado.", e);
		}		
	}

	@Override
	public void update(Estado estado) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(estado);
			transaction.commit();			
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al actualizar el estado.", e);
		}		
	}

	@Override
	public void delete(Estado estado) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(estado);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al eliminar el estado.", e);
		}		
	}

	@Override
	public Estado findById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(Estado.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar el estado por ID.", e);
		}
	}

	@Override
	public List<Estado> findAll() {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			List<Estado> estados = session.createQuery("FROM Estado", Estado.class).getResultList();						
			transaction.commit();
			return estados;
		} catch (Exception e) {
            throw new DAOException("Error al obtener todos los estados.", e);
		}		
	}
}
