package emissarybackend;

class EmissaryUserNotFoundException extends RuntimeException {
	EmissaryUserNotFoundException(Long id) {
		super("Could not find user with id " + id);
	}

	private EmissaryUserNotFoundException(String desc) {
		super(desc);
	}

	public static EmissaryUserNotFoundException fromName(String name) {
		return new EmissaryUserNotFoundException(
			"Could not find user with name " + name);
	}
}
