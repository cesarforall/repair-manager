package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Reparacion;
import model.Repuesto;
import model.RepuestoReparacion;
import model.RepuestoReparacionId;
import util.HibernateUtil;

public class RepuestoReparacionDAO implements GenericDAO<RepuestoReparacion> {

	public void save(RepuestoReparacion partRepair) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();

	        // Obtener la referencia de las clases mientras la sesión está abierta
	        partRepair.setReparacion(session.get(Reparacion.class, partRepair.getReparacion().getIdReparacion()));
	        partRepair.setRepuesto(session.get(Repuesto.class, partRepair.getRepuesto().getIdRepuesto()));

	        session.persist(partRepair);

	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null && transaction.isActive()) {
	            transaction.rollback();
	        }
	        throw new DAOException("Error al guardar RepuestoReparacion.", e);
	    }
	}

	@Override
	public void update(RepuestoReparacion entity) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(entity);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al actualizar RepuestoReparacion.", e);
		}
	}

	@Override
	public void delete(RepuestoReparacion entity) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.remove(session.contains(entity) ? entity : session.merge(entity));
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DAOException("Error al eliminar RepuestoReparacion.", e);
		}
	}
	
	public void deleteByReparacion(int idReparacion) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        session.beginTransaction();
	        String hql = "DELETE FROM RepuestoReparacion WHERE reparacion.id = :idReparacion";
	        session.createMutationQuery(hql)
	               .setParameter("idReparacion", idReparacion)
	               .executeUpdate();
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        throw new DAOException("Error al eliminar repuestos de la reparación.", e);
	    }
	}

	@Override
	public RepuestoReparacion findById(int id) {
		throw new UnsupportedOperationException("Usa findById(RepuestoReparacionId id) para esta entidad.");
	}

	@Override
	public List<RepuestoReparacion> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM RepuestoReparacion", RepuestoReparacion.class).list();
		} catch (Exception e) {
			throw new DAOException("Error al obtener todos los RepuestoReparacion.", e);
		}
	}

	public RepuestoReparacion findById(RepuestoReparacionId id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(RepuestoReparacion.class, id);
		} catch (Exception e) {
			throw new DAOException("Error al buscar RepuestoReparacion por ID compuesto.", e);
		}
	}
	
	public RepuestoReparacion findByReparacionAndRepuesto(int idReparacion, int idRepuesto) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "FROM RepuestoReparacion WHERE reparacion.id = :idReparacion AND repuesto.id = :idRepuesto";
	        return session.createQuery(hql, RepuestoReparacion.class)
	                     .setParameter("idReparacion", idReparacion)
	                     .setParameter("idRepuesto", idRepuesto)
	                     .uniqueResult();
	    }
	}
	
	public List<RepuestoReparacion> findByReparacion(int idReparacion) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "FROM RepuestoReparacion WHERE reparacion.idReparacion = :idReparacion";
	        return session.createQuery(hql, RepuestoReparacion.class)
	            .setParameter("idReparacion", idReparacion)
	            .list();
	    } catch (Exception e) {
	        throw new DAOException("Error al obtener repuestos por reparación.", e);
	    }
	}
}
