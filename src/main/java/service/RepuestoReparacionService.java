package service;

import java.util.List;

import dao.RepuestoReparacionDAO;
import model.RepuestoReparacion;
import model.RepuestoReparacionId;

public class RepuestoReparacionService implements IGenericService<RepuestoReparacion> {

    private RepuestoReparacionDAO dao;

    public RepuestoReparacionService() {
        this.dao = new RepuestoReparacionDAO();
    }

    @Override
    public void save(RepuestoReparacion repuestoReparacion) {
        try {
            RepuestoReparacion existente = dao.findByReparacionAndRepuesto(
                repuestoReparacion.getReparacion().getIdReparacion(),
                repuestoReparacion.getRepuesto().getIdRepuesto()
            );

            if (existente != null) {
                existente.setCantidad(existente.getCantidad() + repuestoReparacion.getCantidad());
                dao.update(existente);
            } else {
                dao.save(repuestoReparacion);
            }
        } catch (Exception e) {
            throw new ServiceException("Error al guardar RepuestoReparacion.", e);
        }
    }

    @Override
    public void update(RepuestoReparacion repuestoReparacion) {
        try {
            dao.update(repuestoReparacion);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar RepuestoReparacion.", e);
        }
    }

    @Override
    public void delete(RepuestoReparacion repuestoReparacion) {
        try {
            dao.delete(repuestoReparacion);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar RepuestoReparacion.", e);
        }
    }

    @Override
    public RepuestoReparacion findById(int id) {
        throw new UnsupportedOperationException("Usa findById(RepuestoReparacionId id) para esta entidad.");
    }

    public RepuestoReparacion findById(RepuestoReparacionId id) {
        try {
            return dao.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar RepuestoReparacion por ID.", e);
        }
    }

    @Override
    public List<RepuestoReparacion> findAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error al obtener todos los RepuestoReparacion.", e);
        }
    }
}
