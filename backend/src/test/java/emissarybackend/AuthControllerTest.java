package emissarybackend;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import io.restassured.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmissaryUserRepository userRepository;

	final EmissaryUser user = new EmissaryUser("MyUser", "MyPassword");

	MvcResult doRegister() throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
			.content(General.asJsonString(user))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andReturn();
	}

	void isResultGood(MvcResult result) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		String json = result.getResponse().getContentAsString();
		final ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(json, Map.class);
		assertEquals(2, map.size());
		assertTrue(map.containsKey("jwt-token"));
		assertTrue(map.containsKey("userId"));
	}

	@Test
	@Order(1)
	void register() throws Exception {
		/*
		var code = RestAssured.given()
			.params("name", user.getName(), 
					"password", user.getPassword())
			.when()
			.post("/auth/register").getStatusCode();
		assertEquals(200, code);
		*/
		MvcResult result = doRegister();
		isResultGood(result);
		Optional<EmissaryUser> maybeUser = userRepository.findByName(user.getName());
		EmissaryUser newUser = maybeUser.get();
		assertAll("registration", 
			() -> assertTrue(maybeUser.isPresent()),
			() -> assertEquals(user.getName(), newUser.getName()),
			() -> assertFalse(user.getPassword().equals(newUser.getPassword())));
	}

	@Test
	@Order(2)
	void login() throws Exception {
		LoginCredentials login = new LoginCredentials(user.getName(), user.getPassword());
		Optional<EmissaryUser> maybeUser = userRepository.findByName(login.getUsername());
		assertTrue(maybeUser.isPresent());

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
			.content(General.asJsonString(login))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andReturn();
		isResultGood(result);
	}

	@Test
	@Order(3)
	void registerUserAlreadyExists() throws Exception {
		MvcResult result = doRegister();

		String json = result.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(json, Map.class);
		assertEquals(1, map.size());
		Map.Entry<String, String> error = Map.entry("error", "A user with this name already exists");
		assertTrue(map.get(error.getKey()) != null);
		assertEquals(error.getValue(), map.get(error.getKey()));
	}
}
