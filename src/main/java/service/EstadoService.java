package service;

import java.util.List;

import dao.EstadoDAO;
import model.Estado;

public class EstadoService {

    private EstadoDAO EstadoDAO;

    public EstadoService() {
        EstadoDAO = new EstadoDAO();
    }

    public void save(Estado Estado) {
        try {
            EstadoDAO.save(Estado);
        } catch (Exception e) {
            throw new ServiceException("Error en EstadoService al guardar el estado.", e);
        }
    }    

    public void update(Estado Estado) {
        try {
            EstadoDAO.update(Estado);
        } catch (Exception e) {
            throw new ServiceException("Error en EstadoService al actualizar el estado.", e);
        }
    }

    public void delete(Estado Estado) {
        try {
            EstadoDAO.delete(Estado);
        } catch (Exception e) {
        	throw new ServiceException("Error en EstadoService al eliminar el estado.", e);
        }
    }
    
    public Estado findById(int id) {
        try {
            return EstadoDAO.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Error en EstadoService al obtener el estado.", e);
        }
    }

    public List<Estado> findAll() {
        try {
            return EstadoDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error en EstadoService al obtener todos los estados.", e);
        }
    }
}