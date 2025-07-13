package service;

import java.util.List;

import dao.ClienteDAO;
import dao.DispositivoDAO;
import dao.EstadoDAO;
import dao.ReparacionDAO;
import dao.ComponenteReparacionDAO;
import model.Estado;
import model.Reparacion;

public class ReparacionService {

    private ReparacionDAO reparacionDAO;
    private DispositivoDAO dispositivoDAO;
    private ClienteDAO clienteDAO;
    private EstadoDAO estadoDAO;
    private ComponenteReparacionDAO componenteReparacionDAO;

    public ReparacionService() {
        reparacionDAO = new ReparacionDAO();
        dispositivoDAO = new DispositivoDAO();
        clienteDAO = new ClienteDAO();
        estadoDAO = new EstadoDAO();
        componenteReparacionDAO = new ComponenteReparacionDAO();
    }

    public void save(Reparacion Reparacion) {
        try {
        	if (Reparacion.getDispositivo() == null ||
                    dispositivoDAO.findById(Reparacion.getDispositivo().getIdDispositivo()) == null) {
                    throw new ServiceException("El dispositivo asociado no existe.");
                }

                if (Reparacion.getCliente() == null ||
                    clienteDAO.findById(Reparacion.getCliente().getIdCliente()) == null) {
                    throw new ServiceException("El cliente asociado no existe.");
                }

                if (Reparacion.getEstado() == null ||
                    estadoDAO.findById(Reparacion.getEstado().getIdEstado()) == null) {
                    throw new ServiceException("El estado asociado no existe.");
                }
        	
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

    public void delete(Reparacion reparacion) {
        try {
            componenteReparacionDAO.deleteByReparacion(reparacion.getIdReparacion());

            reparacionDAO.delete(reparacion);
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
    
    public void updateState(Reparacion reparacion, Estado estado) throws ServiceException {
    	reparacion.setEstado(estado);
    	
    	reparacionDAO.update(reparacion);
    }
}