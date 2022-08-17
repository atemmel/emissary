package emissarybackend;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
class ChatMessageController {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	private final ChatMessageRepository chatRepo;

	private final ChatConversationRepository conversationRepo;

	ChatMessageController(ChatMessageRepository chatRepo, ChatConversationRepository conversationRepo) {
		this.chatRepo = chatRepo;
		this.conversationRepo = conversationRepo;
	}

	@GetMapping("/messages")
	public List<ChatMessage> all() {
		return chatRepo.findAll();
	}

	// client publishes to this
	@MessageMapping("/chat/send")
	// client subscribes to this
	@SendTo("/chat")
	@Transactional
	public ChatMessage newMessage(ChatMessage message) {
		message = chatRepo.save(message);
		var convId = message.getConversation().getId();
		var conv = conversationRepo.findById(convId).orElseThrow(() -> new RuntimeException("Could not find message recipient"));
		conv.addMessage(message);
		log.info("Saved new message");
		return message;
	}
}
