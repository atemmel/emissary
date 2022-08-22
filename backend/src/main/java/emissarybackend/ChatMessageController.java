package emissarybackend;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/api")
class ChatMessageController {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	private final ChatMessageRepository chatRepo;

	private final ChatConversationRepository conversationRepo;

	private final ChatMessageAttachmentRepository attachmentRepo;

	ChatMessageController(ChatMessageRepository chatRepo, ChatConversationRepository conversationRepo, ChatMessageAttachmentRepository attachmentRepo) {
		this.chatRepo = chatRepo;
		this.conversationRepo = conversationRepo;
		this.attachmentRepo = attachmentRepo;
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
		log.info("Begin message handling...");
		var contents = message.getContents();
		var attachment = message.getAttachment();
		boolean lacksMessage = contents.isEmpty() || contents.isBlank();
		boolean lacksAttachment = attachment == null || attachment.getBytes().length <= 0;
		if(lacksMessage && lacksAttachment) {
			log.info("Did not store message, as both the attachment and the message contents were empty");
			return null;
		}

		if(!lacksAttachment) {
			log.info("Saved new message attachment");
			message.setAttachment(attachmentRepo.save(attachment));
		}

		message = chatRepo.save(message);
		var convId = message.getConversation().getId();
		var conv = conversationRepo.findById(convId).orElseThrow(() -> new RuntimeException("Could not find message recipient"));
		conv.addMessage(message);
		log.info("Saved new message");
		return message;
	}
}
