package emissarybackend;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/api")
class ChatMessageController {

	private final ChatMessageRepository chatRepo;

	ChatMessageController(ChatMessageRepository chatRepo) {
		this.chatRepo = chatRepo;
	}

	@GetMapping("/messages")
	public List<ChatMessage> all() {
		return chatRepo.findAll();
	}
}
