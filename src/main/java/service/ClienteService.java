package service;

import java.util.List;

import dao.ClienteDAO;
import models.Cliente;

public class ClienteService {
	private ClienteDAO clienteDAO;
	
	public ClienteService() {
		clienteDAO = new ClienteDAO();
	}
	
	public void create(Cliente cliente) {
		clienteDAO.save(cliente);
	}
	
	public void delete(Cliente cliente) {
		clienteDAO.delete(cliente);
	}
	
	public Cliente getClienteById(int id) {
		return clienteDAO.findById(id);
	}
	
    public List<Cliente> getAll() {
        return clienteDAO.findAll();
    }
}
