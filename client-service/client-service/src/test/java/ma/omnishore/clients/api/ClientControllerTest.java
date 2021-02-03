package ma.omnishore.clients.api;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

import junit.framework.AssertionFailedError;
import ma.omnishore.clients.config.WithMockOAuth2Context;
import ma.omnishore.clients.domain.Client;
import ma.omnishore.clients.service.impl.ClientService;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
public class ClientControllerTest {
	
	// cet objet represente un point d'entrée au serveur MVC et de 
	// manipulation de différentes entrées REST de l'application 
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private RestClientController controller;
	
	//Mocker le service <<ClientService>> via @MockBean
	@MockBean
	private ClientService service;
	
	// Instantiation de l'objet Gson pour la sérialisation/désérialisation
	private final Gson gson = new Gson();
	
	// construire une instance de MockMvc en mode standalone et enregistrer un ou 
	// plusieurs controleurs de l'application dans le web context par programmation
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	@WithMockOAuth2Context(authorities = "user")
	public void testGetClients() throws Exception {
		Client c1 = new Client("Test1","Test1","Test1@mail.com","Address 1");
		Client c2 = new Client("Test2","Test2","Test2@mail.com","Address 2");
		when(service.getAllClients()).thenReturn(Stream.of(c1,c2).collect(Collectors.toList()));
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/client")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andReturn();
		// fail("Not yet implemented");
	}
	
	@Test
	public void testGetClient() throws Exception {
		Client c1 = new Client("Test1", "Test1", "Test1@test.ma", "Address 1");
		c1.setId(1L);
		when(service.getClientById(1L)).thenReturn(c1);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/client/1")
												.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		
		JSONAssert.assertEquals(gson.toJson(c1), result.getResponse().getContentAsString(), false);
	}
	
	
	@Test
	public void testCreateClient() throws Exception {
		Client c1 = new Client("Test1", "Test1", "Test1@test.ma", "Address 1");
		when(service.createClient(any())).thenReturn(c1);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/client")
											.accept(MediaType.APPLICATION_JSON)
											.content(gson.toJson(c1))
											.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();
		
		JSONAssert.assertEquals(gson.toJson(c1),result.getResponse().getContentAsString(), false);
	}
	
	
	@Test
	public void testUpdateClient() throws Exception {
		Client c1 = new Client("Test1","Test1", "Test1@tets.ma", "Address 1");
		c1.setId(1L);
		Client c2 = new Client("Test2","Test2", "Test2@tets.ma", "Address 2");
		c2.setId(1L);
		when(service.updateClient(any())).thenReturn(c2);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/client")
											.accept(MediaType.APPLICATION_JSON)
											.content(gson.toJson(c2))
											.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		
		JSONAssert.assertEquals(gson.toJson(c2), result.getResponse().getContentAsString(), false);
		
	}
	
	
	@Test
	public void testDeleteClient() throws Exception {
		doNothing().when(service).deleteClientById(1L);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/client/1")
											.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		
	}
	
	
	
	
	
	
	
	
	
	 
	
	

}
