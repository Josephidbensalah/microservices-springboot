package ma.omnishore.clients.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ma.omnishore.clients.domain.Client;
import ma.omnishore.clients.repository.ClientRepository;
import ma.omnishore.clients.service.IClientService;

@Service
@Transactional
public class ClientService implements IClientService {
	
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public Client createClient(Client c) {
		// return the saved client 
		return clientRepository.save(c);
	}

	@Override
	public Client getClientById(long id) {
		// return the Client 
		return clientRepository.findById(id).get();
	}

	@Override
	public List<Client> getAllClients(Pageable pageable) {
		// return the list of clients
		return clientRepository.findAll();
	}

	@Override
	public Client updateClient(Client c) {
		// update the client and return it 
		return clientRepository.save(c);
	}

	@Override
	public void deleteClientById(long id) {
		// delete Client by ID
		clientRepository.deleteById(id);
	}

	@Override
	public void deleteAllClients() {
		// delete all Clients
		clientRepository.deleteAll();
	}

	@Override
	public List<Client> getAllClients() {
		// return the list of clients
		return clientRepository.findAll();
	}

}
