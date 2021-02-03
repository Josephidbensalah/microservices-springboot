package ma.omnishore.clients.api;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.omnishore.clients.api.feign.SalesClient;
import ma.omnishore.clients.domain.Client;
import ma.omnishore.clients.dto.SaleDto;
import ma.omnishore.clients.service.IClientService;

@RestController
@RequestMapping("/api/client")
public class RestClientController {
	
	@Autowired
	private IClientService clientService;
	
	@Autowired
	private SalesClient salesClient;
	
	private static final Logger log = LoggerFactory.getLogger(RestClientController.class);
	
	//--------------------- get All Clients -----------------------------
	@PreAuthorize("hasRole('user')")
	@GetMapping
	public ResponseEntity<List<Client>> findAll(){
		log.info("Returning client list from database.");
		
		//SecurityContextHolder.getContext().getAuthentication();
		List<Client> clients = clientService.getAllClients();
		return new ResponseEntity<>(clients, HttpStatus.OK);
	}
	
	//----------------------- get single Client -----------------------------
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Client> getClient(@PathVariable ("id") long id) {
		log.info("Returning the client with the ID="+id);
		Client client = clientService.getClientById(id);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}
	
	//---------------------- Create a new Client -----------------------------
	@PostMapping
	public ResponseEntity<Client> createClient(@RequestBody Client client ){
		log.info("Creating a new Client and saving it to database.");
		 client = clientService.createClient(client);
		 return new ResponseEntity<Client>(client, HttpStatus.CREATED);
	}
	
	//-------------------- Update a client ----------------------------------
	@PutMapping
	public ResponseEntity<Client> updateClient(@RequestBody Client client ){
		log.info("Updating a client .");
		client = clientService.updateClient(client);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}
	
	//-------------------------Delete a client --------------------------------
	@DeleteMapping(value="/{id}")
	public ResponseEntity<HttpStatus> deleteClient(@PathVariable long id) {
		log.info("deleting client from database.");
		clientService.deleteClientById(id);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	//--------------Delete all Clients -----------------------------
	@DeleteMapping
	public ResponseEntity<HttpStatus> deleteAllClient(){
		log.info("deleting all clients from database.");
		clientService.deleteAllClients();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//----------------------Retrieve Client Sales ---------------------------
	@CircuitBreaker(name = "sales-service",fallbackMethod = "salesFailed")
	@GetMapping(value="/{id}/sales")
	public List<SaleDto> getClientSales(@PathVariable("id") long id){
		return  salesClient.getClientSales(id);	 
	}
	
	public List<SaleDto> salesFailed(long id , Throwable throwable){
		return new ArrayList<>();
	}
	
}
