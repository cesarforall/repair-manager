package dao;

import java.beans.Statement;
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
			if (transaction != null) transaction.rollback();
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
			if (transaction != null) transaction.rollback();
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
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al eliminar el dispositivo en la base de datos.", e);
		}		
	}

	@Override
	public Dispositivo findById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(Dispositivo.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar el dispositivo por ID en la base de datos.", e);
		}
	}

	@Override
	public List<Dispositivo> findAll() {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			List<Dispositivo> dispositivos = session.createQuery("FROM Dispositivo", Dispositivo.class).getResultList();						
			transaction.commit();
			return dispositivos;
		} catch (Exception e) {
			throw new DAOException("Error al listar los dispositivos en la base de datos.", e);
		}		
	}
	
	public List<Dispositivo> findAllAvailable() {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			String hql = """
				    SELECT d
				    FROM Dispositivo d
				    WHERE NOT EXISTS (
				        SELECT 1
				        FROM Reparacion r
				        WHERE r.dispositivo = d
				        AND LOWER(r.estado.nombre) <> 'cerrada'
				    )
				    """;
			List<Dispositivo> dispositivos = session.createQuery(hql, Dispositivo.class).getResultList();						
			transaction.commit();
			return dispositivos;
		} catch (Exception e) {
			throw new DAOException("Error al listar los dispositivos en la base de datos.", e);
		}		
	}
}
