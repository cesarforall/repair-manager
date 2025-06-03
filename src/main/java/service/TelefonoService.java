package service;

import java.util.List;

import dao.TelefonoDAO;
import model.Telefono;

public class TelefonoService {

    private TelefonoDAO telefonoDAO;

    public TelefonoService() {
        telefonoDAO = new TelefonoDAO();
    }

    public void save(Telefono telefono) {
        try {
            telefonoDAO.save(telefono);
        } catch (Exception e) {
        	throw new ServiceException("Error en TelefonoService al guardar el teléfono.", e);
        }
    }    

    public void update(Telefono telefono) {
        try {
            telefonoDAO.update(telefono);
        } catch (Exception e) {
        	throw new ServiceException("Error en TelefonoService al actualizar el teléfono.", e);
        }
    }

    public void delete(Telefono telefono) {
        try {
            telefonoDAO.delete(telefono);
        } catch (Exception e) {
        	throw new ServiceException("Error en TelefonoService al eliminar el teléfono.", e);
        }
    }
    
    public Telefono findById(int id) {
        try {
            return telefonoDAO.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Error en TelefonoService al obtener el teléfono.", e);
        }
    }

    public List<Telefono> findAll() {
        try {
            return telefonoDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error en TelefonoService al obtener todos los teléfonos.", e);
        }
    }
}