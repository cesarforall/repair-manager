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
            System.err.println("Error en ReparacionService al guardar el Reparacion.");
            e.printStackTrace();
        }
    }    

    public void update(Reparacion Reparacion) {
        try {
            reparacionDAO.update(Reparacion);
        } catch (Exception e) {
            System.err.println("Error en ReparacionService al actualizar el Reparacion.");
            e.printStackTrace();
        }
    }

    public void delete(Reparacion Reparacion) {
        try {
            reparacionDAO.delete(Reparacion);
        } catch (Exception e) {
            System.err.println("Error en ReparacionService al eliminar la Reparación.");
            e.printStackTrace();
        }
    }
    
    public Reparacion findById(int id) {
        try {
            return reparacionDAO.findById(id);
        } catch (Exception e) {
            System.err.println("Error en ReparacionService al obtener el Reparación.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Reparacion> findAll() {
        try {
            return reparacionDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error en ReparacionService al obtener todos las Reparaciones.");
            e.printStackTrace();
            return null;
        }
    }
}