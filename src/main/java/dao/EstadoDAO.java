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
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al guardar el estado.");
			e.printStackTrace();
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
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al actualizar el estado.");
            e.printStackTrace();
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
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error al eliminar el estado.");
			e.printStackTrace();
		}		
	}

	@Override
	public Estado findById(int id) {
		Estado estado = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			estado = session.get(Estado.class, id);			
		} catch (Exception e) {
			System.err.println("Error al buscar el estado por ID.");
            e.printStackTrace();
		}
		return estado;
	}

	@Override
	public List<Estado> findAll() {
		List<Estado> estados = null;
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			estados = session.createQuery("FROM Estado", Estado.class).getResultList();						
			transaction.commit();
		} catch (Exception e) {
			System.err.println("Error al obtener todos los estados.");
            e.printStackTrace();
		}		
		return estados;
	}
}
