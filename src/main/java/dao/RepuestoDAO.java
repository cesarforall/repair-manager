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
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al guardar el repuesto.");
			e.printStackTrace();
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
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al actualizar el repuesto.");
            e.printStackTrace();
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
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al eliminar el repuesto.");
			e.printStackTrace();
		}		
	}

	@Override
	public Repuesto findById(int id) {
		Repuesto repuesto = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			repuesto = session.get(Repuesto.class, id);			
		} catch (Exception e) {
			System.err.println("Error al buscar el repuesto por ID.");
            e.printStackTrace();
		}
		return repuesto;
	}

	@Override
	public List<Repuesto> findAll() {
		List<Repuesto> repuestos = null;
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			repuestos = session.createQuery("FROM REPUESTOS", Repuesto.class).getResultList();						
			transaction.commit();
		} catch (Exception e) {
			System.err.println("Error al obtener todos los repuestos.");
            e.printStackTrace();
		}		
		return repuestos;
	}
}
