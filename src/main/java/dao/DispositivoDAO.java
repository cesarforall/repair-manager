package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Dispositivo;
import util.HibernateUtil;

public class DispositivoDAO implements GenericDAO<Dispositivo>{
	@Override
	public void save(Dispositivo dispositivo) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			session.persist(dispositivo);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al guardar el dispositivo.");
			e.printStackTrace();
		}		
	}

	@Override
	public void update(Dispositivo dispositivo) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(dispositivo);
			transaction.commit();			
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al actualizar el dispositivo.");
            e.printStackTrace();
		}		
	}

	@Override
	public void delete(Dispositivo dispositivo) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(dispositivo);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al eliminar el dispositivo.");
			e.printStackTrace();
		}		
	}

	@Override
	public Dispositivo findById(int id) {
		Dispositivo dispositivo = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			dispositivo = session.get(Dispositivo.class, id);			
		} catch (Exception e) {
			System.err.println("Error al buscar el dispositivo por ID.");
            e.printStackTrace();
		}
		return dispositivo;
	}

	@Override
	public List<Dispositivo> findAll() {
		List<Dispositivo> dispositivos = null;
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			dispositivos = session.createQuery("FROM Dispositivo", Dispositivo.class).getResultList();						
			transaction.commit();
		} catch (Exception e) {
			System.err.println("Error al obtener todos los dispositivos.");
            e.printStackTrace();
		}		
		return dispositivos;
	}
}
