package emissarybackend;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/api")
class ChatMessageController {

	private final ChatMessageRepository chatRepo;
	private final ChatMessageAttachmentRepository attachmentRepo;

	ChatMessageController(ChatMessageRepository chatRepo,
			ChatMessageAttachmentRepository attachmentRepo) {
		this.chatRepo = chatRepo;
		this.attachmentRepo = attachmentRepo;
	}

	@GetMapping("/messages")
	public List<ChatMessage> all() {
		return chatRepo.findAll();
	}

	@GetMapping("/messages/{id}")
	public ChatMessage one(@PathVariable Long id) {
		return chatRepo.findById(id).orElseThrow(
				() -> new RuntimeException("Chat message with id " + id + " not found"));
	}

	@GetMapping("/message/fromAttachment/{id}")
	public ChatMessage fromAttachment(@PathVariable Long id) {
		final var attachment = attachmentRepo.findById(id).orElseThrow(
				() -> new ChatMessageAttachmentNotFoundException(id));
		return chatRepo.findByAttachment(attachment).orElseThrow(
				() -> new RuntimeException("Chat message not found"));
	}
}
