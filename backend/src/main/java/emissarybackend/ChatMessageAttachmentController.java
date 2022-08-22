package emissarybackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	private final ChatMessageAttachmentRepository repo;

	@Autowired
	public ChatMessageAttachmentController(ChatMessageAttachmentRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/attachments/{id}")
	public ChatMessageAttachment one(@PathVariable Long id) {
		return repo.findById(id).orElseThrow(
			() -> new RuntimeException("Could not find attachment with id " + id)
		);
	}

	@RequestMapping(value="/attachments/create",
		method=RequestMethod.POST,
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void create(@RequestParam("file") MultipartFile document) {
		log.info("Uploaded type: " + document.getContentType());
		//TODO: work this out
	}
	/*
	public void create(@RequestBody MultiValueMap<String, String> formData) {
		final var keys = formData.keySet();
		var it = keys.iterator();
		while(it.hasNext()) {
			var key = it.next();
			var value = formData.get(key);
			log.info("Found key: " + key + " value: " + value.toString());
		}
	}
	*/

}
