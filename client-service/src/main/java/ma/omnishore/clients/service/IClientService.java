package ma.omnishore.clients.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import ma.omnishore.clients.domain.Client;

public interface IClientService {
	
	// Create new Client
	public Client createClient(Client c);
	
	// Get client by ID
	public Client getClientById(long id);
	
	// Get All Clients
	public List<Client> getAllClients(Pageable pageable);
	
	// Update Client
	public Client updateClient(Client c);
	
	// Delete Client By ID
	public void deleteClientById(long id);
	
	// Delete All Clients
	public void deleteAllClients();

	public List<Client> getAllClients();
}
