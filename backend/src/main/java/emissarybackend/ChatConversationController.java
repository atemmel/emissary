package emissarybackend;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class ChatConversationController {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
	private final ChatConversationRepository repo;
	private final EmissaryUserRepository userRepo;

	ChatConversationController(ChatConversationRepository repo, EmissaryUserRepository userRepo) {
		this.repo = repo;
		this.userRepo = userRepo;
	}

	@GetMapping("/conversations")
	public List<ChatConversation> all() {
		return repo.findAll();
	}

	@GetMapping("/conversations/{id}/catchup")
	public List<ChatMessage> catchup(
			@PathVariable("id") Long conversationId,
			@RequestParam("from") int from) {
		final var conversation = repo.findById(conversationId).orElseThrow(
			() -> new ChatConversationNotFoundException(conversationId));
		final var messages = conversation.getMessages();
		return messages.subList(from, messages.size());
	}

	@GetMapping("/conversations/{id}/head")
	public List<ChatMessage> head(
			@PathVariable("id") Long conversationId,
			@RequestParam("count") int count) {
		final var conversation = repo.findById(conversationId).orElseThrow(
			() -> new ChatConversationNotFoundException(conversationId));
		final var messages = conversation.getMessages();
		final var begin = messages.size() - (messages.size() - count < 0 
			? messages.size()
			: count);
		log.info("Sending " + begin + " to " + messages.size());
		return messages.subList(begin, messages.size());
	}

	@GetMapping("/conversations/{conversationId}/init")
	public ChatConversation init(
			@PathVariable Long conversationId,
			@RequestParam("count") int count) {
		final var conversation = repo.findById(conversationId).orElseThrow(
			() -> new ChatConversationNotFoundException(conversationId));
		final var messages = head(conversationId, count);
		final var result = new ChatConversation(conversation);
		result.setMessages(messages);
		return result;
	}

	@GetMapping("/conversations/{id}")
	public ChatConversation one(@PathVariable Long id) {
		return repo.findById(id).orElseThrow(
			() -> new ChatConversationNotFoundException(id));
	}

	@PostMapping("/conversations/create")
	public void create(@RequestBody ChatConversation conversation) {
		log.info("Saved new conversation with " 
			+ conversation.getParticipants().size()
			+ " participants");
		repo.save(conversation);
	}

	@PostMapping("/conversations/addParticipants")
	public void addParticipants(@RequestBody ConversationUsersDelta delta) {
		log.info("Adding " + delta.users.size() + " users to a conversation");
		final var conversationId = delta.getConversationId();
		var conversation = repo.findById(conversationId).orElseThrow(
				() -> new ChatConversationNotFoundException(conversationId));
		for (var userId : delta.getUsers()) {
			var user = userRepo.findById(userId).orElseThrow(
					() -> new EmissaryUserNotFoundException(userId));
			conversation.addParticipant(user);
		}
		repo.save(conversation);
	}

	@PostMapping("/conversations/removeParticipants")
	public void removeParticipants(@RequestBody ConversationUsersDelta delta) {
		log.info("Removing " + delta.users.size() + " users from a conversation");
		final var conversationId = delta.getConversationId();
		var conversation = repo.findById(conversationId).orElseThrow(
				() -> new ChatConversationNotFoundException(conversationId));
		for(var userId : delta.getUsers()) {
			var user = userRepo.findById(userId).orElseThrow(
					() -> new EmissaryUserNotFoundException(userId));
			conversation.removeParticipant(user);
		}
		repo.save(conversation);
	}

	public static class ConversationUsersDelta  {
		private List<Long> users = new ArrayList<>();
		private Long conversationId;

		ConversationUsersDelta() {
		}

		List<Long> getUsers() {
			return users;
		}

		void setUsers(List<Long> users) {
			this.users = users;
		}

		Long getConversationId() {
			return conversationId;
		}

		void setConversationId(Long conversationId) {
			this.conversationId = conversationId;
		}
	}
}
