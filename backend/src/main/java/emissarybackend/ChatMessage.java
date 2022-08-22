package emissarybackend;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class ChatMessage {
	private @Id @GeneratedValue Long id;
	
	private String contents;

	@OneToOne
	private ChatMessageAttachment attachment;

	@ManyToOne
	@JoinColumn(name = "CHAT_CONVERSATION_ID")
	@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id", 
		resolver = EntityIdResolver.class, 
		scope = ChatConversation.class)
	@JsonIdentityReference(alwaysAsId = true)
	private ChatConversation conversation;

	@ManyToOne
	@JoinColumn(name = "EMISSARY_USER_ID")
	@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id", 
		resolver = EntityIdResolver.class, 
		scope = EmissaryUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	private EmissaryUser author;

	private Date timestamp = new Date();

	public ChatMessage() {
		contents = "";
		id = 0l;
		timestamp = new Date();
		attachment = null;
	}

	public ChatMessage(ChatMessage other) {
		this.contents = other.contents;
		this.id = other.id;
		this.conversation = other.conversation;
		this.author = other.author;
		this.attachment = other.attachment;
	}

	public ChatMessage(String contents, EmissaryUser author) {
		this.contents = contents;
		this.author = author;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public ChatMessageAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(ChatMessageAttachment attachment) {
		this.attachment = attachment;
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

	public EmissaryUser getAuthor() {
		return author;
	}

	public void setAuthor(EmissaryUser author) {
		this.author = author;
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
