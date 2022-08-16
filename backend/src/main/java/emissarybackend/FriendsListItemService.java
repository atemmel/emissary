package emissarybackend;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FriendsListItemService")
public class FriendsListItemService {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Autowired
	EmissaryUserRepository userRepository;

	@Autowired
	ChatConversationRepository conversationRepository;

	@Autowired
	ChatMessageRepository messageRepository;

	List<FriendsListItem> createFriendsListItemsByUserId(Long userId) {
		final EmissaryUser user = userRepository.findById(userId).orElseThrow(
			() -> new RuntimeException("Could not find user with id " + userId));
		List<FriendsListItem> items = new ArrayList<>();
		log.info("Total conversations " + user.getConversations().size());
		var it = user.getConversations().iterator();
		while(it.hasNext()) {
			var conv = it.next();
			var messages = conv.getMessages();

			// ska konversationer utan meddelanden få finnas?
			assert(messages.size() > 0);

			var lastMessage = messages.get(messages.size() - 1);
			var lastAuthor = lastMessage.getAuthor();

			var item = new FriendsListItem(conv.getId(), 
				lastAuthor.getName(), lastMessage.getContents());
			items.add(item);
		}

		return items;
	}
}
