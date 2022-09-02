package emissarybackend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private final PollVoteRepository voteRepository;

	RealtimeController(
			EmissaryUserRepository userRepo,
			ChatMessageRepository chatRepo, 
			ChatConversationRepository conversationRepo,
			ChatMessageAttachmentRepository attachmentRepo,
			PollVoteRepository voteRepository) {
		this.userRepo = userRepo;
		this.chatRepo = chatRepo;
		this.conversationRepo = conversationRepo;
		this.attachmentRepo = attachmentRepo;
		this.voteRepository = voteRepository;
	}

	// client publishes to this
	@MessageMapping("/chat/send")
	// client subscribes to this
	@SendTo("/chat")
	@Transactional
	public Map<String, Object> newMessage(ChatMessage message) {
		log.info("Begin message handling...");
		final String contents = message.getContents();
		final ChatMessageAttachment attachment = message.getAttachment();
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
		final Long convId = message.getConversation().getId();
		final ChatConversation conv = conversationRepo.findById(convId).orElseThrow(
				() -> new ChatConversationNotFoundException(convId));
		conv.addMessage(message);
		log.info("Saved new message");
		return Map.of("message", message);
	}

	@MessageMapping("/chat/vote")
	@SendTo("/chat")
	@Transactional
	public Map<String, Object> newVote(PollVote vote) {

		final EmissaryUser author = vote.getAuthor();
		final ChatMessage poll = vote.getPoll();
		final ChatMessageAttachment attachment = poll.getAttachment();
		final List<PollVote> votes = voteRepository.findByPoll(poll);
		PollVote prevVote = null;
		for(PollVote other: votes) {
			if(other.getAuthor().getId() == author.getId()) {
				prevVote = other;
				break;
			}
		}

		final Map<String, Integer> map = attachment.getPoll();
		if(prevVote != null) {
			Integer voteCount = map.get(prevVote.getChoice());
			map.put(prevVote.getChoice(), voteCount - 1);
			voteRepository.deleteById(prevVote.getId());
		}
		Integer voteCount = map.get(vote.getChoice());
		map.put(vote.getChoice(), voteCount + 1);
		attachmentRepo.save(attachment);
		vote = voteRepository.save(vote);

		log.info("Voted " + vote.getChoice());
		return Map.of("vote", vote);
	}

	@MessageMapping("/chat/askhead/")
	@SendTo("/chat/head")
	@Transactional
	public Map<String, Object> head(
			@RequestParam("conversationId") Long conversationId,
			@RequestParam("userId") Long userId) {
		final ChatConversation conversation = conversationRepo.findById(conversationId).orElseThrow(
			() -> new ChatConversationNotFoundException(conversationId));
		final int conversationHead = conversation.getMessages().size();
		final EmissaryUser user = userRepo.findById(userId).orElseThrow(
			() -> new EmissaryUserNotFoundException(userId));
		final Date friendsListHead = getFriendsListHead(user);
		return Map.of(
			"conversationHead", conversationHead,
			"friendsListHead", friendsListHead);
	}

	@MessageMapping("/chat/askheads/{id}")
	@SendTo("/chat/heads")
	@Transactional
	public Map<String, Object> heads(
			@PathVariable("userId") Long userId) {
		final EmissaryUser user = userRepo.findById(userId).orElseThrow(
				() -> new EmissaryUserNotFoundException(userId));
		final Map<Long, Integer> conversationHeads = getConversationHeads(user);
		final Date friendsListHead = getFriendsListHead(user);
		return Map.of(
			"conversationHeads", conversationHeads,
			"friendsListHead", friendsListHead);
	}

	Date getFriendsListHead(EmissaryUser user) {
		final Set<ChatConversation> conversations = user.getConversations();
		if(conversations.isEmpty()) {
			return new Date(0);
		}
		Date latest = new Date(0);
		for(final ChatConversation conversation : conversations) {
			if(conversation.getMessages().isEmpty()) {
				latest = latest.before(conversation.getCreationTimestamp())
					? conversation.getCreationTimestamp()
					: latest;
				continue;
			}
			
			final ChatMessage lastMessage = conversation.lastMessage();
			latest = latest.before(lastMessage.getTimestamp())
				? lastMessage.getTimestamp()
				: latest;
		}
		return latest;
	}

	Map<Long, Integer> getConversationHeads(EmissaryUser user) {
		final Set<ChatConversation> conversations = user.getConversations();
		if(conversations.isEmpty()) {
			return Map.of();
		}
		Map<Long, Integer> latest = new HashMap<Long, Integer>(conversations.size());
		for(final ChatConversation conversation: conversations) {
			final Long id = conversation.getId();
			final int head = conversation.getMessages().size();
			latest.put(id, head);
		}
		return latest;
	}
}
