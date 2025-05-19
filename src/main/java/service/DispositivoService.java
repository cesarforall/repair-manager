package service;

import java.util.List;

import dao.DispositivoDAO;
import model.Dispositivo;

public class DispositivoService {

    private DispositivoDAO dispositivoDAO;

    public DispositivoService() {
        dispositivoDAO = new DispositivoDAO();
    }

    public void save(Dispositivo dispositivo) {
        try {
            dispositivoDAO.save(dispositivo);
        } catch (Exception e) {
        	throw new ServiceException("No se pudo completar la creación del dispositivo.", e.getCause());
        }
    }    

    public void update(Dispositivo dispositivo) {
        try {
            dispositivoDAO.update(dispositivo);
        } catch (Exception e) {
            System.err.println("Error en DispositivoService al actualizar el dispositivo.");
            e.printStackTrace();
        }
    }

    public void delete(Dispositivo dispositivo) {
        try {
            dispositivoDAO.delete(dispositivo);
        } catch (Exception e) {
            System.err.println("Error en DispositivoService al eliminar el dispositivo.");
            e.printStackTrace();
        }
    }
    
    public Dispositivo findById(int id) {
        try {
            return dispositivoDAO.findById(id);
        } catch (Exception e) {
            System.err.println("Error en DispositivoService al obtener el dispositivo.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Dispositivo> findAll() {
        try {
            return dispositivoDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error en DispositivoService al obtener todos los dispositivos.");
            e.printStackTrace();
            return null;
        }
    }
}