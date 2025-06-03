package service;

import java.util.List;

import dao.RepuestoDAO;
import model.Repuesto;

public class RepuestoService {

    private RepuestoDAO repuestoDAO;

    public RepuestoService() {
        repuestoDAO = new RepuestoDAO();
    }

    public void save(Repuesto repuesto) {
        try {
            repuestoDAO.save(repuesto);
        } catch (Exception e) {
            throw new ServiceException("Error en RepuestoService al guardar el repuesto.", e);
        }
    }    

    public void update(Repuesto repuesto) {
        try {
            repuestoDAO.update(repuesto);
        } catch (Exception e) {
        	throw new ServiceException("Error en RepuestoService al actualizar el repuesto.", e);
        }
    }

    public void delete(Repuesto repuesto) {
        try {
            repuestoDAO.delete(repuesto);
        } catch (Exception e) {
        	throw new ServiceException("Error en RepuestoService al eliminar el repuesto.", e);
        }
    }
    
    public Repuesto findById(int id) {
        try {
            return repuestoDAO.findById(id);
        } catch (Exception e) {
        	throw new ServiceException("Error en RepuestoService al obtener el repuesto.", e);
        }
    }

    public List<Repuesto> findAll() {
        try {
            return repuestoDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error en RepuestoService al obtener todos los repuestos.", e);
        }
    }
}