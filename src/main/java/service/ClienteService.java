package service;

import java.util.List;

import dao.ClienteDAO;
import dao.TelefonoDAO;
import model.Cliente;
import model.Telefono;

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

    public void delete(Cliente cliente) {
        try {
            clienteDAO.delete(cliente);
        } catch (Exception e) {
            System.err.println("Error en ClienteService al eliminar el cliente.");
            e.printStackTrace();
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
