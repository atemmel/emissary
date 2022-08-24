package emissarybackend;

class ChatConversationNotFoundException extends RuntimeException {
	ChatConversationNotFoundException(Long id) {
		super("Could not find conversation with id " + id);
	}
}
