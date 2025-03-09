package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import models.Cliente;
import utils.HibernateUtil;

public class ClienteDAO implements GenericDAO<Cliente>{
	private Session getSession() {
		return HibernateUtil.getSessionFactory().openSession();
	}

	@Override
	public void save(Cliente cliente) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		
		session.persist(cliente);
		
		transaction.commit();
		session.close();		
	}

	@Override
	public void update(Cliente entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Cliente entity) {
		// TODO Auto-generated method stub
		
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
