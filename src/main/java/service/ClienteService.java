package service;

import java.util.List;

import dao.ClienteDAO;
import models.Cliente;

public class ClienteService {
	private ClienteDAO clienteDAO;
	
	public ClienteService() {
		clienteDAO = new ClienteDAO();
	}
	
	public Cliente getClienteById(int id) {
		return clienteDAO.findById(id);
	}
	
    public List<Cliente> getAll() {
        return clienteDAO.findAll();
    }
}
