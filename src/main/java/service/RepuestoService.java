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
            System.err.println("Error en RepuestoService al guardar el repuesto.");
            e.printStackTrace();
        }
    }    

    public void update(Repuesto repuesto) {
        try {
            repuestoDAO.update(repuesto);
        } catch (Exception e) {
            System.err.println("Error en RepuestoService al actualizar el repuesto.");
            e.printStackTrace();
        }
    }

    public void delete(Repuesto repuesto) {
        try {
            repuestoDAO.delete(repuesto);
        } catch (Exception e) {
            System.err.println("Error en RepuestoService al eliminar el repuesto.");
            e.printStackTrace();
        }
    }
    
    public Repuesto findById(int id) {
        try {
            return repuestoDAO.findById(id);
        } catch (Exception e) {
            System.err.println("Error en RepuestoService al obtener el repuesto.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Repuesto> findAll() {
        try {
            return repuestoDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error en RepuestoService al obtener todos los repuestos.");
            e.printStackTrace();
            return null;
        }
    }
}