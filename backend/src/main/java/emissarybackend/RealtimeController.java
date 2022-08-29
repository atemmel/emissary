package emissarybackend;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class RealtimeController {
	private static final Logger log = LoggerFactory.getLogger(RealtimeController.class);

	private final EmissaryUserRepository userRepo;
	private final ChatMessageRepository chatRepo;
	private final ChatConversationRepository conversationRepo;
	private final ChatMessageAttachmentRepository attachmentRepo;

	RealtimeController(
			EmissaryUserRepository userRepo,
			ChatMessageRepository chatRepo, 
			ChatConversationRepository conversationRepo,
			ChatMessageAttachmentRepository attachmentRepo) {
		this.userRepo = userRepo;
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
		boolean lacksAttachment = attachment == null 
			|| (attachment.getBytes().length <= 0 
			&& attachment.getPoll() == null);
		if(lacksMessage && lacksAttachment) {
			log.info("Did not store message, as both the attachment and the message contents were empty");
			return null;
		}

		if(!lacksAttachment) {
			log.info("Saved new message attachment");
			if(attachment.getPoll() != null) {
				log.info("Poll has " + attachment.getPoll().size() + " options");
			}
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

	@MessageMapping("/chat/askhead/")
	@SendTo("/chat/head")
	@Transactional
	public Map<String, Object> head(
			@RequestParam("conversationId") Long conversationId,
			@RequestParam("userId") Long userId) {
		final var conversation = conversationRepo.findById(conversationId).orElseThrow(
			() -> new ChatConversationNotFoundException(conversationId));
		final var conversationHead = conversation.getMessages().size();
		final var user = userRepo.findById(userId).orElseThrow(
			() -> new EmissaryUserNotFoundException(userId));
		final var friendsListHead = getFriendsListHead(user);
		return Map.of(
			"conversationHead", conversationHead,
			"friendsListHead", friendsListHead);
	}

	@MessageMapping("/chat/askheads/{id}")
	@SendTo("/chat/heads")
	@Transactional
	public Map<String, Object> heads(
			@PathVariable("userId") Long userId) {
		final var user = userRepo.findById(userId).orElseThrow(
				() -> new EmissaryUserNotFoundException(userId));
		final var conversationHeads = getConversationHeads(user);
		final var friendsListHead = getFriendsListHead(user);
		return Map.of(
			"conversationHeads", conversationHeads,
			"friendsListHead", friendsListHead);
	}

	Date getFriendsListHead(EmissaryUser user) {
		final var conversations = user.getConversations();
		if(conversations.isEmpty()) {
			return new Date(0);
		}
		var latest = new Date(0);
		for(final var conversation : conversations) {
			if(conversation.getMessages().isEmpty()) {
				latest = latest.before(conversation.getCreationTimestamp())
					? conversation.getCreationTimestamp()
					: latest;
				continue;
			}
			
			final var lastMessage = conversation.lastMessage();
			latest = latest.before(lastMessage.getTimestamp())
				? lastMessage.getTimestamp()
				: latest;
		}
		return latest;
	}

	Map<Long, Integer> getConversationHeads(EmissaryUser user) {
		final var conversations = user.getConversations();
		if(conversations.isEmpty()) {
			return Map.of();
		}
		var latest = new HashMap<Long, Integer>(conversations.size());
		for(final var conversation: conversations) {
			final var id = conversation.getId();
			final var head = conversation.getMessages().size();
			latest.put(id, head);
		}
		return latest;
	}
}
