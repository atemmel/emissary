package emissarybackend;

class ChatMessageAttachmentNotFoundException extends RuntimeException {
	ChatMessageAttachmentNotFoundException(Long id) {
		super("Could not find attachment with id " + id);
	}
}
