package service;

import java.util.List;

import dao.ComponenteDAO;
import model.Componente;

public class ComponenteService {

    private ComponenteDAO componenteDAO;

    public ComponenteService() {
        componenteDAO = new ComponenteDAO();
    }

    public void save(Componente componente) {
        try {
            componenteDAO.save(componente);
        } catch (Exception e) {
            throw new ServiceException("Error en ComponenteService al guardar el componente.", e);
        }
    }    

    public void update(Componente componente) {
        try {
            componenteDAO.update(componente);
        } catch (Exception e) {
        	throw new ServiceException("Error en ComponenteService al actualizar el componente.", e);
        }
    }

    public void delete(Componente componente) {
        try {
            componenteDAO.delete(componente);
        } catch (Exception e) {
        	throw new ServiceException("Error en ComponenteService al eliminar el componente.", e);
        }
    }
    
    public Componente findById(int id) {
        try {
            return componenteDAO.findById(id);
        } catch (Exception e) {
        	throw new ServiceException("Error en ComponenteService al obtener el componente.", e);
        }
    }

    public List<Componente> findAll() {
        try {
            return componenteDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error en ComponenteService al obtener todos los componentes.", e);
        }
    }
}