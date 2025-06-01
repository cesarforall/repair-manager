package service;

import java.util.List;

import dao.ReparacionDAO;
import model.Reparacion;

public class ReparacionService {

    private ReparacionDAO reparacionDAO;

    public ReparacionService() {
        reparacionDAO = new ReparacionDAO();
    }

    public void save(Reparacion Reparacion) {
        try {
            reparacionDAO.save(Reparacion);
        } catch (Exception e) {
        	throw new ServiceException("Error en ReparacionService al guardar la reparación.", e);
        }
    }    

    public void update(Reparacion Reparacion) {
        try {
            reparacionDAO.update(Reparacion);
        } catch (Exception e) {
        	throw new ServiceException("Error en ReparacionService al actualizar la reparación.", e);
        }
    }

    public void delete(Reparacion Reparacion) {
        try {
            reparacionDAO.delete(Reparacion);
        } catch (Exception e) {
        	throw new ServiceException("Error en ReparacionService al eliminar la reparación.", e);
        }
    }
    
    public Reparacion findById(int id) {
        try {
            return reparacionDAO.findById(id);
        } catch (Exception e) {
        	throw new ServiceException("Error en ReparacionService al obtener la reparación por ID.", e);
        }
    }

    public List<Reparacion> findAll() {
        try {
            return reparacionDAO.findAll();
        } catch (Exception e) {
        	throw new ServiceException("Error en ReparacionService al obtener todas las Reparaciones.", e);
        }
    }
}