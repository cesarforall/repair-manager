package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Cliente;
import util.HibernateUtil;

public class ClienteDAO implements GenericDAO<Cliente>{
	private Session getSession() {
		return HibernateUtil.getSessionFactory().openSession();
	}

	@Override
	public void save(Cliente cliente) {		
		try (Session session = getSession()) {
			Transaction transaction = session.beginTransaction();
			
			session.persist(cliente);
			
			transaction.commit();
		} catch (Exception e) {
			System.err.println("Error en dao.");
			e.printStackTrace();
		}		
	}

	@Override
	public void update(Cliente entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Cliente cliente) {
		Transaction transaction = null;
		
		try (Session session = getSession()) {
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
		Session session = getSession();
		Cliente cliente = session.get(Cliente.class, id);
		session.close();
		return cliente;
	}

	@Override
	public List<Cliente> findAll() {
		List<Cliente> clientes = null;
		
		try (Session session = getSession();) {
			Transaction transaction = session.beginTransaction();
			
			clientes = session.createQuery("FROM CLIENTES", Cliente.class).getResultList();
						
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return clientes;
	}
}
