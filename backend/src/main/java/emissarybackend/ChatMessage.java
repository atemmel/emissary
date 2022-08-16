package emissarybackend;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ChatMessage {
	private @Id @GeneratedValue Long id;
	
	private String contents;

	//@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHAT_CONVERSATION_ID")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private ChatConversation conversation;

	public ChatMessage() {
		contents = "";
		id = 0l;
	}

	public ChatMessage(ChatMessage other) {
		this.contents = other.contents;
		this.id = other.id;
		this.conversation = other.conversation;
	}

	public ChatMessage(String contents) {
		this.contents = contents;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChatConversation getConversation() {
		return conversation;
	}

	public void setConversation(ChatConversation conversation) {
		this.conversation = conversation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.contents,
			this.id);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}

		if(!(o instanceof ChatMessage)) {
			return false;
		}

		var that = (ChatMessage)o;
		return Objects.equals(this.contents, that.contents)
			&& Objects.equals(this.id, that.id);
	}
}
