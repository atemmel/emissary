package emissarybackend;

import java.util.Comparator;
import java.util.Date;

public class FriendsListItem {
	private Long conversationId;
	private String friendName;
	private String prevMessage;
	private String prevAuthor;
	private Date timestamp;

	public FriendsListItem() {

	}

	public FriendsListItem(Long id, String name, String msg, String prevAuthor, Date timestamp) {
		this.conversationId = id;
		this.friendName = name;
		this.prevMessage = msg;
		this.timestamp = timestamp;
		this.prevAuthor = prevAuthor;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getPrevMessage() {
		return prevMessage;
	}

	public void setPrevMessage(String prevMessage) {
		this.prevMessage = prevMessage;
	}

	public String getPrevAuthor() {
		return prevAuthor;
	}

	public void setPrevMessage(String prevAuthor) {
		this.prevAuthor = prevAuthor;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	static public class CompareDateDescending implements Comparator<FriendsListItem> {
		@Override
		public int compare(FriendsListItem lhs, FriendsListItem rhs) {
			return rhs.getTimestamp().compareTo(lhs.getTimestamp());
		}
	}
}
