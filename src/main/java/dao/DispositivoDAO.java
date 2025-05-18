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
			throw new DAOException("Error al guardar el dispositivo en la base de datos.", e);
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
			throw new DAOException("Error al modificar el dispositivo en la base de datos.", e);
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
			throw new DAOException("Error al eliminar el dispositivo en la base de datos.", e);
		}		
	}

	@Override
	public Dispositivo findById(int id) {
		Dispositivo dispositivo = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			dispositivo = session.get(Dispositivo.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar el dispositivo por ID en la base de datos.", e);
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
			throw new DAOException("Error al listar los dispositivos en la base de datos.", e);
		}		
		return dispositivos;
	}
}
