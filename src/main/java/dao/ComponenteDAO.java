package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Componente;
import util.HibernateUtil;

public class ComponenteDAO implements GenericDAO<Componente>{
	@Override
	public void save(Componente componente) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			session.persist(componente);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al guardar el componente.", e);
		}		
	}

	@Override
	public void update(Componente componente) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(componente);
			transaction.commit();			
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al actualizar el componente.", e);
		}		
	}

	@Override
	public void delete(Componente componente) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(componente);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al eliminar el componente.", e);
		}		
	}

	@Override
	public Componente findById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(Componente.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar el componente por ID.", e);
		}
	}

	@Override
	public List<Componente> findAll() {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			List<Componente> componentes = session.createQuery("FROM Componente", Componente.class).getResultList();						
			transaction.commit();
			return componentes;
		} catch (Exception e) {
			throw new DAOException("Error al obtener todos los componentes.", e);
		}		
	}
}
