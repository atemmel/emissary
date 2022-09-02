package emissarybackend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FriendsListItemService")
public class FriendsListItemService {
	private static final Logger log = LoggerFactory.getLogger(FriendsListItemService.class);

	@Autowired
	EmissaryUserRepository userRepository;

	@Autowired
	ChatConversationRepository conversationRepository;

	@Autowired
	ChatMessageRepository messageRepository;

	String createFriendNameFromParticipants(final Set<EmissaryUser> users, Long currentUserId) {
		Set<EmissaryUser> otherUsers = new HashSet<EmissaryUser>(users);
		otherUsers.removeIf((usr) -> usr.getId() == currentUserId);
		String friendName = "";
		Iterator<EmissaryUser> it = otherUsers.iterator();
		while(it.hasNext()) {
			EmissaryUser usr = it.next();
			friendName += usr.getName();
			if(it.hasNext()) {
				friendName += ", ";
			}
		}
		return friendName;
	}

	List<FriendsListItem> createFriendsListItemsByUserId(Long userId) {
		final EmissaryUser user = userRepository.findById(userId).orElseThrow(
			() -> new EmissaryUserNotFoundException(userId));
		List<FriendsListItem> items = new ArrayList<>();
		log.info("Total conversations " + user.getConversations().size());
		Iterator<ChatConversation> it = user.getConversations().iterator();
		while(it.hasNext()) {
			ChatConversation conv = it.next();
			List<ChatMessage> messages = conv.getMessages();

			if(messages.size() > 0) {
				ChatMessage lastMessage = messages.get(messages.size() - 1);
				EmissaryUser lastAuthor = lastMessage.getAuthor();
				String friendName = createFriendNameFromParticipants(conv.getParticipants(), userId);
				FriendsListItem item = new FriendsListItem(
					conv.getId(), 
					friendName,
					lastMessage.getContents(), 
					lastAuthor.getName(), 
					lastMessage.getTimestamp());
					items.add(item);
			} else {
				String lastMessage = "No messages sent";
				String friendName = createFriendNameFromParticipants(conv.getParticipants(), userId);
				FriendsListItem item = new FriendsListItem(
					conv.getId(),
					friendName,
					lastMessage,
					"",
					conv.getCreationTimestamp());
				items.add(item);
			}

		}

		items.sort(new FriendsListItem.CompareDateDescending());

		return items;
	}
}
