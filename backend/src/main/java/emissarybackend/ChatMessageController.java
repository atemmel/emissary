package emissarybackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
class ChatMessageController {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	private final ChatMessageRepository repo;

	ChatMessageController(ChatMessageRepository repo) {
		this.repo = repo;
	}

	// client publishes to this
	@MessageMapping("/chat")
	// client subscribes to this
	@SendTo("/chat/send")
	//public ChatMessage newMessage() {
	public ChatMessage newMessage(ChatMessage message) {
		log.info("HERE");
		return repo.save(message);
	}
}
