package emissarybackend;

class ConversationNotFoundException extends RuntimeException {
	ConversationNotFoundException(Long id) {
		super("Could not find conversation with id " + id);
	}
}
