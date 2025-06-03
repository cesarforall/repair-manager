package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Repuesto;
import util.HibernateUtil;

public class RepuestoDAO implements GenericDAO<Repuesto>{
	@Override
	public void save(Repuesto repuesto) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			session.persist(repuesto);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al guardar el repuesto.", e);
		}		
	}

	@Override
	public void update(Repuesto repuesto) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(repuesto);
			transaction.commit();			
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al actualizar el repuesto.", e);
		}		
	}

	@Override
	public void delete(Repuesto repuesto) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(repuesto);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al eliminar el repuesto.", e);
		}		
	}

	@Override
	public Repuesto findById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(Repuesto.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar el repuesto por ID.", e);
		}
	}

	@Override
	public List<Repuesto> findAll() {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			List<Repuesto> repuestos = session.createQuery("FROM Repuesto", Repuesto.class).getResultList();						
			transaction.commit();
			return repuestos;
		} catch (Exception e) {
			throw new DAOException("Error al obtener todos los repuestos.", e);
		}		
	}
}
