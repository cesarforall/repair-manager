package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Reparacion;
import util.HibernateUtil;

public class ReparacionDAO implements GenericDAO<Reparacion>{
	@Override
	public void save(Reparacion reparacion) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			session.persist(reparacion);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new DAOException("Error al guardar la reparación.", e);
		}		
	}

	@Override
	public void update(Reparacion reparacion) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(reparacion);
			transaction.commit();			
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new DAOException("Error al actualizar la reparación.", e);
		}		
	}

	@Override
	public void delete(Reparacion reparacion) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(reparacion);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new DAOException("Error al eliminar la reparación.", e);
		}		
	}

	@Override
	public Reparacion findById(int id) {
		Reparacion reparacion = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			reparacion = session.get(Reparacion.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar la reparación por ID.", e);
		}
		return reparacion;
	}

	@Override
	public List<Reparacion> findAll() {
		List<Reparacion> repairs = null;
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			repairs = session.createQuery("FROM Reparacion", Reparacion.class).getResultList();						
			transaction.commit();
		} catch (Exception e) {
			throw new DAOException("Error al buscar todas las reparaciones.", e);
		}		
		return repairs;
	}
}
