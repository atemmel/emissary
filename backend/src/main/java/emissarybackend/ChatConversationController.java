package emissarybackend;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ChatConversationController {
	private final ChatConversationRepository repo;

	ChatConversationController(ChatConversationRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/conversations")
	List<ChatConversation> all() {
		return repo.findAll();
	}

	@GetMapping("/conversations/{id}")
	ChatConversation one(@PathVariable Long id) {
		return repo.findById(id).orElseThrow(
			() -> new RuntimeException("Could not find conversation with id " + id)
		);
	}

	public class FriendsListItem {
		private Long friendId;
		private String friendName;
		private String prevMessage;
	}
}
