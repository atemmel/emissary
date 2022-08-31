package emissarybackend;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private EmissaryUserRepository emissaryUserRepository;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Map<String, Object> register(EmissaryUser user) {
		var maybeUser = emissaryUserRepository.findByName(user.getName());
		if(maybeUser.isPresent()) {
			log.info("Could not register user " + user.getName() + ", user already exists");
			return Collections.singletonMap("error", "A user with this name already exists");
		}
		var encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		log.info("Saving " + user.getName());
		user = emissaryUserRepository.save(user);
		String token = jwtUtil.generateToken(user.getName());
		log.info("Registering " + user.getName());
		return createLoginInfoMap(token, user);
	}

	public Map<String, Object> login(LoginCredentials body) {
		log.info("Logging in " + body.getUsername());
		try {
			var authInputToken = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
			log.info("Authenticating... ");
			authManager.authenticate(authInputToken);
			log.info("Generating token... ");
			String token = jwtUtil.generateToken(body.getUsername());
			log.info("Finding " + body.getUsername());
			var user = emissaryUserRepository.findByName(body.getUsername()).orElseThrow(
					() -> new RuntimeException("Could not find user by name"));
			log.info("Success!");
			return createLoginInfoMap(token, user);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid login credentials");
		}
	}


	private static Map<String, Object> createLoginInfoMap(String token, EmissaryUser user) {
		var map = new HashMap<String, Object>();
		map.put("jwt-token", token);
		map.put("userId", user.getId());
		return map;
	}
}
