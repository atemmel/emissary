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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private EmissaryUserRepository userRepository;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public Map<String, Object> registerHandler(@RequestBody EmissaryUser user) {
		var maybeUser = userRepository.findByName(user.getName());
		if(maybeUser.isPresent()) {
			log.info("Could not register user " + user.getName() + ", user already exists");
			return Collections.singletonMap("error", "A user with this name already exists");
		}
		var encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		log.info("Saving " + user.getName());
		user = userRepository.save(user);
		String token = jwtUtil.generateToken(user.getName());
		log.info("Registering " + user.getName());
		//return Collections.singletonMap("jwt-token", token);
		return createLoginInfoMap(token, user);
	}

	@PostMapping("/login")
	public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
		try {
			var authInputToken = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
			authManager.authenticate(authInputToken);
			String token = jwtUtil.generateToken(body.getUsername());
			//return Collections.singletonMap("jwt-token", token);
			var user = userRepository.findByName(body.getUsername()).orElseThrow(() -> new RuntimeException("Could not find user by name"));
			return createLoginInfoMap(token, user);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid login credentials");
		}
	}

	public Map<String, Object> createLoginInfoMap(String token, EmissaryUser user) {
		var map = new HashMap<String, Object>();
		map.put("jwt-token", token);
		map.put("userId", user.getId());
		return map;
	}
}
