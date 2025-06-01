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
            System.err.println("Error en ClienteService al guardar el cliente.");
            e.printStackTrace();
        }
    }
    
    public void saveClientWithPhone(Cliente cliente, Telefono telefono) {
        try {
            clienteDAO.save(cliente);
            if (telefono != null) {
                telefonoDAO.save(telefono);
            }
        } catch (Exception e) {
            System.err.println("Error en ClienteService al guardar el cliente con teléfono.");
            e.printStackTrace();
        }
    }

    public void update(Cliente cliente) {
        try {
            clienteDAO.update(cliente);
        } catch (Exception e) {
            System.err.println("Error en ClienteService al actualizar el cliente.");
            e.printStackTrace();
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
            System.err.println("Error en ClienteService al buscar el cliente por ID.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Cliente> findAll() {
        try {
            return clienteDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error en ClienteService al obtener todos los clientes.");
            e.printStackTrace();
            return null;
        }
    }
}
