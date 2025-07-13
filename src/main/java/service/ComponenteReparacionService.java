package service;

import java.util.List;

import dao.ComponenteReparacionDAO;
import model.ComponenteReparacion;
import model.ComponenteReparacionId;

public class ComponenteReparacionService implements IGenericService<ComponenteReparacion> {

    private ComponenteReparacionDAO dao;

    public ComponenteReparacionService() {
        this.dao = new ComponenteReparacionDAO();
    }

    @Override
    public void save(ComponenteReparacion componenteReparacion) {
        try {
            ComponenteReparacion existente = dao.findByReparacionAndComponente(
                componenteReparacion.getReparacion().getIdReparacion(),
                componenteReparacion.getComponente().getidComponente()
            );

            if (existente != null) {
                existente.setCantidad(existente.getCantidad() + componenteReparacion.getCantidad());
                dao.update(existente);
            } else {
                dao.save(componenteReparacion);
            }
        } catch (Exception e) {
            throw new ServiceException("Error al guardar ComponenteReparacion.", e);
        }
    }

    @Override
    public void update(ComponenteReparacion componenteReparacion) {
        try {
            dao.update(componenteReparacion);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar ComponenteReparacion.", e);
        }
    }

    @Override
    public void delete(ComponenteReparacion componenteReparacion) {
        try {
            dao.delete(componenteReparacion);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar ComponenteReparacion.", e);
        }
    }

    @Override
    public ComponenteReparacion findById(int id) {
        throw new UnsupportedOperationException("Usa findById(ComponenteReparacionId id) para esta entidad.");
    }

    public ComponenteReparacion findById(ComponenteReparacionId id) {
        try {
            return dao.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar ComponenteReparacion por ID.", e);
        }
    }
    
    public List<ComponenteReparacion> findByRepairId(int repairId) {
        try {
            return dao.findByReparacion(repairId);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener componentes por reparación.", e);
        }
    }

    @Override
    public List<ComponenteReparacion> findAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error al obtener todos los ComponenteReparacion.", e);
        }
    }
    
    public double calculateTotalByRepair(int idReparacion) {
        try {
            List<ComponenteReparacion> parts = dao.findByReparacion(idReparacion);
            double total = 0;
            for (ComponenteReparacion part : parts) {
                total += part.getCantidad() * part.getComponente().getPrecio();
            }
            return total;
        } catch (Exception e) {
            throw new ServiceException("Error al calcular el total de componentes.", e);
        }
    }
}
