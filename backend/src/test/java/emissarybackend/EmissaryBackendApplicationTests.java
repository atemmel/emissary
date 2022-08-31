package emissarybackend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmissaryBackendApplicationTests {
	@Autowired
	private AuthController authController;
	@Autowired
	private AuthService authService;
	@Autowired
	private EmissaryUserRepository emissaryUserRepository;

	@Test
	void contextLoads() {
		assertTrue(authController != null);
		assertTrue(authService != null);
		assertTrue(emissaryUserRepository != null);
	}

}
