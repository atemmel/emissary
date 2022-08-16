package emissarybackend;

public class FriendsListItem {
	private Long conversationId;
	private String friendName;
	private String prevMessage;

	public FriendsListItem() {

	}

	public FriendsListItem(Long id, String name, String msg) {
		this.conversationId = id;
		this.friendName = name;
		this.prevMessage = msg;
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
}
