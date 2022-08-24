package emissarybackend;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class EmissaryUserController {
	private final EmissaryUserRepository repo;

	EmissaryUserController(EmissaryUserRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/users")
	List<EmissaryUser> all() {
		return repo.findAll();
	}

	@GetMapping("/users/{id}")
	EmissaryUser one(@PathVariable Long id) {
		return repo.findById(id).orElseThrow(
			() -> new UserNotFoundException(id));
	}
}
