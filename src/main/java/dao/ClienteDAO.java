package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Cliente;
import util.HibernateUtil;

public class ClienteDAO implements GenericDAO<Cliente>{
	@Override
	public void save(Cliente cliente) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			session.persist(cliente);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error en dao.");
			e.printStackTrace();
		}		
	}

	@Override
	public void update(Cliente cliente) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(cliente);
			transaction.commit();			
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			// Error message
		}		
	}

	@Override
	public void delete(Cliente cliente) {
		Transaction transaction = null;		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(cliente);			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.err.println("Error en dao.");
			e.printStackTrace();
		}		
	}

	@Override
	public Cliente findById(int id) {
		Cliente cliente = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			cliente = session.get(Cliente.class, id);			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cliente;
	}

	@Override
	public List<Cliente> findAll() {
		List<Cliente> clientes = null;
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();			
			clientes = session.createQuery("FROM CLIENTES", Cliente.class).getResultList();						
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return clientes;
	}
}
