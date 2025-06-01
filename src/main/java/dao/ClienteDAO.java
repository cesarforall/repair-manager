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
			throw new DAOException("Error al guardar el cliente.", e);
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
			throw new DAOException("Error al actualizar el cliente.", e);
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
			throw new DAOException("Error al eliminar el cliente.", e);
		}		
	}

	@Override
	public Cliente findById(int id) {
		Cliente cliente = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			cliente = session.get(Cliente.class, id);			
		} catch (Exception e) {
			throw new DAOException("Error al buscar el cliente por ID.", e);
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
			throw new DAOException("Error al obtener todos los clientes.", e);
		}		
		return clientes;
	}
	
	public boolean hasPhoneByClient(int clientId) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "SELECT count(t.idTelefono) FROM Telefono t WHERE t.cliente.id = :clientId";
	        Long count = session.createQuery(hql, Long.class)
	            .setParameter("clientId", clientId)
	            .uniqueResult();
	        return count != null && count > 0;
	    } catch (Exception e) {
	        throw new DAOException("Error verificando teléfonos por cliente", e);
	    }
	}

	public boolean hasRepairByClient(int clientId) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "SELECT count(r.idReparacion) FROM Reparacion r WHERE r.cliente.id = :clientId";
	        Long count = session.createQuery(hql, Long.class)
	            .setParameter("clientId", clientId)
	            .uniqueResult();
	        return count != null && count > 0;
	    } catch (Exception e) {
	        throw new DAOException("Error verificando reparaciones por cliente", e);
	    }
	}
}
