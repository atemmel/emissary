package emissarybackend;

class AttachmentNotFoundException extends RuntimeException {
	AttachmentNotFoundException(Long id) {
		super("Could not find attachment with id " + id);
	}
}
