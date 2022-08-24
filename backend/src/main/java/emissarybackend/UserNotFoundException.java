package emissarybackend;

class UserNotFoundException extends RuntimeException {
	UserNotFoundException(Long id) {
		super("Could not find user with id " + id);
	}

	private UserNotFoundException(String desc) {
		super(desc);
	}

	public static UserNotFoundException fromName(String name) {
		return new UserNotFoundException(
			"Could not find user with name " + name);
	}
}
