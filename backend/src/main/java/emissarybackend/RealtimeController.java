package emissarybackend;

import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class RealtimeController {
	private static final Logger log = LoggerFactory.getLogger(RealtimeController.class);

	private final ChatMessageRepository chatRepo;
	private final ChatConversationRepository conversationRepo;
	private final ChatMessageAttachmentRepository attachmentRepo;

	RealtimeController(ChatMessageRepository chatRepo, 
			ChatConversationRepository conversationRepo,
			ChatMessageAttachmentRepository attachmentRepo) {
		this.chatRepo = chatRepo;
		this.conversationRepo = conversationRepo;
		this.attachmentRepo = attachmentRepo;
	}

	// client publishes to this
	@MessageMapping("/chat/send")
	// client subscribes to this
	@SendTo("/chat")
	@Transactional
	public ChatMessage newMessage(ChatMessage message) {
		log.info("Begin message handling...");
		final var contents = message.getContents();
		final var attachment = message.getAttachment();
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
		final var convId = message.getConversation().getId();
		final var conv = conversationRepo.findById(convId).orElseThrow(
				() -> new ChatConversationNotFoundException(convId));
		conv.addMessage(message);
		log.info("Saved new message");
		return message;
	}

	@MessageMapping("/chat/askhead/{id}")
	@SendTo("/chat/head")
	@Transactional
	public Map<String, Object> head(@PathVariable("id") Long conversationId) {
		final var conversation = conversationRepo.findById(conversationId).orElseThrow(
			() -> new ChatConversationNotFoundException(conversationId));
		final var head = conversation.getMessages().size();
		return Map.of("head", head);
	}

}
