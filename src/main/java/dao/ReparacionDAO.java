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
			System.err.println("Error al guardar el reparacion.");
			e.printStackTrace();
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
			System.err.println("Error al actualizar el reparacion.");
            e.printStackTrace();
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
			System.err.println("Error al eliminar el reparacion.");
			e.printStackTrace();
		}		
	}

	@Override
	public Reparacion findById(int id) {
		Reparacion reparacion = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			reparacion = session.get(Reparacion.class, id);			
		} catch (Exception e) {
			System.err.println("Error al buscar el reparacion por ID.");
            e.printStackTrace();
		}
		return reparacion;
	}

	@Override
	public List<Reparacion> findAll() {
		List<Reparacion> reparacions = null;
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			reparacions = session.createQuery("FROM reparacionS", Reparacion.class).getResultList();						
			transaction.commit();
		} catch (Exception e) {
			System.err.println("Error al obtener todos los reparacions.");
            e.printStackTrace();
		}		
		return reparacions;
	}
}
