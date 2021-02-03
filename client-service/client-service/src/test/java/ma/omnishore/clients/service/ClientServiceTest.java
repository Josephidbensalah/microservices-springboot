package ma.omnishore.clients.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ma.omnishore.clients.ClientServiceApplication;
import ma.omnishore.clients.domain.Client;
import ma.omnishore.clients.repository.ClientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {ClientServiceApplication.class})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientServiceTest {

		@Autowired
		private ClientRepository clientRepository;
		
		@Autowired
		private IClientService clientService;
		
		private Long randomId;
		
		@Before
		public void createClient() {
			Client randomClient = new Client("Test1","Test1","test1@test.com","address 1");
			randomId = clientRepository.save(randomClient).getId();
		}
		
		@After
		public void deleteClients() {
			clientRepository.deleteAll();
		}
		
		@Test
		public void verifyGetAllClients() {
			List<Client> clients = clientService.getAllClients();
			assertThat(clients, hasSize(1));
		}
		
		@Test
		public void testGetClient() {
			Client per = clientService.getClientById(randomId);
			assertNotNull(per.getId());
		}
		
		@Test
		public void testCreateClient() throws Exception {
			Client randomClient2 = new Client("Test2","Test2","Test2@test.com","Address 2");
			Client client = clientService.createClient(randomClient2);
			assertNotNull(client.getId());
		}
		
		@Test 
		public void testUpdateClient() throws Exception {
			Client ranClient3 = new Client("Test2", "Test3", "Test3@test.com", "Address 3 ");
			ranClient3.setId(randomId);
			Client client = clientService.updateClient(ranClient3);
			assertEquals(client.getFirstName(),ranClient3.getFirstName());
		}
		
		@Test
		public void testDeleteClient() throws Exception {
			clientService.deleteClientById(randomId);
		}
}




