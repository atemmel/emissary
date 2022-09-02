package emissarybackend;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmissaryUserRepository userRepository;

	final EmissaryUser user = new EmissaryUser("MyUser", "MyPassword");

	@Test
	@Order(1)
	void register() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
			.content(General.asJsonString(user))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		var maybeUser = userRepository.findByName(user.getName());
		var newUser = maybeUser.get();
		assertAll("registration", 
			() -> assertTrue(maybeUser.isPresent()),
			() -> assertEquals(user.getName(), newUser.getName()),
			() -> assertFalse(user.getPassword().equals(newUser.getPassword())));
	}

	@Test
	@Order(2)
	void login() throws Exception {
		var login = new LoginCredentials(user.getName(), user.getPassword());
		var maybeUser = userRepository.findByName(login.getUsername());
		assertTrue(maybeUser.isPresent());

		var result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
			.content(General.asJsonString(login))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andReturn();
		
		var json = result.getResponse().getContentAsString();
		final var mapper = new ObjectMapper();
		var map = mapper.readValue(json, Map.class);
		assertEquals(2, map.size());
		assertTrue(map.containsKey("jwt-token"));
		assertTrue(map.containsKey("userId"));
	}

}
