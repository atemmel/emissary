package emissarybackend;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class ChatConversationController {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
	private final ChatConversationRepository repo;

	ChatConversationController(ChatConversationRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/conversations")
	public List<ChatConversation> all() {
		return repo.findAll();
	}

	@GetMapping("/conversations/{id}")
	public ChatConversation one(@PathVariable Long id) {
		return repo.findById(id).orElseThrow(
			() -> new RuntimeException("Could not find conversation with id " + id)
		);
	}

	@PostMapping("conversations/create")
	public void create(@RequestBody ChatConversation conversation) {
		log.info("Saved new conversation with " 
			+ conversation.getParticipants().size()
			+ " participants");
		repo.save(conversation);
	}
}
