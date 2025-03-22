package service;

import java.util.List;

import dao.ClienteDAO;
import model.Cliente;

public class ClienteService {
	private ClienteDAO clienteDAO;
	
    public void save(Cliente cliente) {
        try {
            clienteDAO.save(cliente);
        } catch (Exception e) {
            System.err.println("Error en ClienteService al guardar el cliente.");
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
