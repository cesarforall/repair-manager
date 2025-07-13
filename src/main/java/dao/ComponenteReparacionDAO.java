package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Reparacion;
import model.Componente;
import model.ComponenteReparacion;
import model.ComponenteReparacionId;
import util.HibernateUtil;

public class ComponenteReparacionDAO implements GenericDAO<ComponenteReparacion> {

	public void save(ComponenteReparacion partRepair) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();

	        // Obtener la referencia de las clases mientras la sesión está abierta
	        partRepair.setReparacion(session.get(Reparacion.class, partRepair.getReparacion().getIdReparacion()));
	        partRepair.setComponente(session.get(Componente.class, partRepair.getComponente().getidComponente()));

	        session.persist(partRepair);

	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null && transaction.isActive()) {
	            transaction.rollback();
	        }
	        throw new DAOException("Error al guardar ComponenteReparacion.", e);
	    }
	}

	@Override
	public void update(ComponenteReparacion entity) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(entity);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al actualizar ComponenteReparacion.", e);
		}
	}

	@Override
	public void delete(ComponenteReparacion entity) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(session.contains(entity) ? entity : session.merge(entity));
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al eliminar ComponenteReparacion.", e);
		}
	}
	
	public void deleteByReparacion(int idReparacion) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        session.beginTransaction();
	        String hql = "DELETE FROM ComponenteReparacion WHERE reparacion.id = :idReparacion";
	        session.createMutationQuery(hql)
	               .setParameter("idReparacion", idReparacion)
	               .executeUpdate();
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        throw new DAOException("Error al eliminar componentes de la reparación.", e);
	    }
	}

	@Override
	public ComponenteReparacion findById(int id) {
		throw new UnsupportedOperationException("Usa findById(ComponenteReparacionId id) para esta entidad.");
	}

	@Override
	public List<ComponenteReparacion> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM ComponenteReparacion", ComponenteReparacion.class).list();
		} catch (Exception e) {
			throw new DAOException("Error al obtener todos los ComponenteReparacion.", e);
		}
	}

	public ComponenteReparacion findById(ComponenteReparacionId id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(ComponenteReparacion.class, id);
		} catch (Exception e) {
			throw new DAOException("Error al buscar ComponenteReparacion por ID compuesto.", e);
		}
	}
	
	public ComponenteReparacion findByReparacionAndComponente(int idReparacion, int idComponente) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "FROM ComponenteReparacion WHERE reparacion.id = :idReparacion AND componente.id = :idComponente";
	        return session.createQuery(hql, ComponenteReparacion.class)
	                     .setParameter("idReparacion", idReparacion)
	                     .setParameter("idComponente", idComponente)
	                     .uniqueResult();
	    }
	}
	
	public List<ComponenteReparacion> findByReparacion(int idReparacion) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "FROM ComponenteReparacion WHERE reparacion.idReparacion = :idReparacion";
	        return session.createQuery(hql, ComponenteReparacion.class)
	            .setParameter("idReparacion", idReparacion)
	            .list();
	    } catch (Exception e) {
	        throw new DAOException("Error al obtener componentes por reparación.", e);
	    }
	}
}
