package emissarybackend;

import java.io.IOException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Controller
@RequestMapping("/api")
class ChatMessageAttachmentController {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	private final ChatMessageRepository chatRepo;
	private final ChatConversationRepository conversationRepo;
	private final ChatMessageAttachmentRepository attachmentRepo;
	private final EmissaryUserRepository userRepo;

	@Autowired
	public ChatMessageAttachmentController(
			ChatMessageRepository chatRepo,
			ChatConversationRepository conversationRepo,
			ChatMessageAttachmentRepository attachmentRepo,
			EmissaryUserRepository userRepo) {
		this.chatRepo = chatRepo;
		this.conversationRepo = conversationRepo;
		this.attachmentRepo = attachmentRepo;
		this.userRepo = userRepo;
	}

	@GetMapping("/attachments/{id}")
	public ChatMessageAttachment one(@PathVariable Long id) {
		return attachmentRepo.findById(id).orElseThrow(
			() -> new ChatMessageAttachmentNotFoundException(id));
	}

	@RequestMapping(value="/attachments/create",
		method=RequestMethod.POST,
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	public ChatMessage create(
			@RequestParam("userId") Long userId, 
			@RequestParam("conversationId") Long conversationId,
			@RequestParam("file") MultipartFile document) {
		log.info("Request for file upload of type: " + document.getContentType());
		log.info("UserId: " + userId);
		log.info("ConversationId: " + conversationId);

		final var size = document.getSize();
		final var mb = 1024l * 1024l;
		final var threshold = 8l * mb;
		if(size > threshold) {
			log.info("Discarded upload request; file to large");
			return null;
		}

		var message = new ChatMessage();
		message.setAuthor(userRepo.findById(userId).orElseThrow(
			() -> new EmissaryUserNotFoundException(userId)));
		var conv = conversationRepo.findById(conversationId).orElseThrow(
			() -> new ChatConversationNotFoundException(conversationId));

		try {
			var attachment = new ChatMessageAttachment(
				document.getBytes(),
				document.getOriginalFilename(),
				document.getContentType());
			message.setAttachment(attachmentRepo.save(attachment));
			message = chatRepo.save(message);
			conv.addMessage(message);
			return message;
		} catch (IOException err) {
			log.info("IOException occured, attachment not created");
		}
		return null;
	}
}
