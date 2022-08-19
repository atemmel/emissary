package emissarybackend;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

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

	String createFriendNameFromParticipants(final Set<EmissaryUser> users, Long currentUserId) {
		var otherUsers = new HashSet<EmissaryUser>(users);
		otherUsers.removeIf((usr) -> usr.getId() == currentUserId);
		var friendName = "";
		var it = otherUsers.iterator();
		while(it.hasNext()) {
			var usr = it.next();
			friendName += usr.getName();
			if(it.hasNext()) {
				friendName += ", ";
			}
		}
		return friendName;
		/*
		for(int i = 0; i < participants.size(); i++) {
			lastAuthor += participants.get(i).getName();
			if(i + 1 < participants.size()) {
				lastAuthor += ", ";
			}
		}
		*/
	}

	List<FriendsListItem> createFriendsListItemsByUserId(Long userId) {
		final EmissaryUser user = userRepository.findById(userId).orElseThrow(
			() -> new RuntimeException("Could not find user with id " + userId));
		List<FriendsListItem> items = new ArrayList<>();
		log.info("Total conversations " + user.getConversations().size());
		var it = user.getConversations().iterator();
		while(it.hasNext()) {
			var conv = it.next();
			var messages = conv.getMessages();

			if(messages.size() > 0) {
				var lastMessage = messages.get(messages.size() - 1);
				var lastAuthor = lastMessage.getAuthor();
				var friendName = createFriendNameFromParticipants(conv.getParticipants(), userId);
				var item = new FriendsListItem(
					conv.getId(), 
					friendName,
					lastMessage.getContents(), 
					lastAuthor.getName(), 
					lastMessage.getTimestamp());
					items.add(item);
			} else {
				var lastMessage = "No messages sent";
				var friendName = createFriendNameFromParticipants(conv.getParticipants(), userId);
				var item = new FriendsListItem(
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
