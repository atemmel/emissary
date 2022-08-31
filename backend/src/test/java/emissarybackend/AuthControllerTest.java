package emissarybackend;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmissaryUserRepository userRepository;

	@Test
	@Order(1)
	void registerAndLogin() throws Exception {
		var user = new EmissaryUser();
		user.setName("Admin");
		user.setPassword("myPassword");

		mockMvc.perform(post("/auth/register")
			.content(General.asJsonString(user))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andDo(print());

		var maybeUser = userRepository.findByName(user.getName());
		assertAll("registration", 
			() -> assertTrue(maybeUser.isPresent()),
			() -> assertEquals(user.getName(), maybeUser.get().getName()),
			() -> assertFalse(user.getPassword().equals(maybeUser.get().getPassword())));
	}

	@Test
	@Order(2)
	void login() throws Exception {
		var login = new LoginCredentials("Admin", "myPassword");

		mockMvc.perform(post("/auth/login")
			.content(General.asJsonString(login))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

}
