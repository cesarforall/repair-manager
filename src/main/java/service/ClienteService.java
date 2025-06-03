package service;

import java.util.List;

import dao.ClienteDAO;
import dao.DAOException;
import dao.TelefonoDAO;
import model.Cliente;
import model.Telefono;
import util.StatusMessage;
import util.StatusMessage.Type;

public class ClienteService {
	private ClienteDAO clienteDAO;
	private TelefonoDAO telefonoDAO;
	
    public ClienteService() {
        clienteDAO = new ClienteDAO();
        telefonoDAO = new TelefonoDAO();
    }
	
    public void save(Cliente cliente) {
        try {
            clienteDAO.save(cliente);
        } catch (Exception e) {
            throw new ServiceException("Error en ClienteService al guardar el cliente.", e);
        }
    }
    
    public void saveClientWithPhone(Cliente cliente, Telefono telefono) {
        try {
            clienteDAO.save(cliente);
            if (telefono != null) {
                telefonoDAO.save(telefono);
            }
        } catch (Exception e) {
            throw new ServiceException("Error en ClienteService al guardar el cliente con teléfono.", e);
        }
    }

    public void update(Cliente cliente) {
        try {
            clienteDAO.update(cliente);
        } catch (Exception e) {
            throw new ServiceException("Error en ClienteService al actualizar el cliente.", e);
        }
    }

    public StatusMessage delete(Cliente cliente) {
        try {
            if (clienteDAO.hasRepairByClient(cliente.getIdCliente())) {
                return new StatusMessage(Type.ERROR, "Cliente no eliminado: tiene reparaciones asociadas.");
            } else {
                clienteDAO.delete(cliente);
                return new StatusMessage(Type.INFO, "Cliente eliminado correctamente.");
            }
        } catch (DAOException e) {
        	 throw new ServiceException(e.getMessage(), e);
        }
    }

    public Cliente findById(int id) {
        try {
            return clienteDAO.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Error en ClienteService al buscar el cliente por ID.", e);
        }
    }

    public List<Cliente> findAll() {
        try {
            return clienteDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error en ClienteService al obtener todos los clientes.", e);
        }
    }
}
