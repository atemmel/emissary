package emissarybackend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class ChatConversation {
	private @Id @GeneratedValue Long id;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="EMISSARY_USER_ID")
	//@JsonIgnore
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = EntityIdResolver.class, scope = EmissaryUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	private Set<EmissaryUser> participants = new HashSet<EmissaryUser>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<ChatMessage> messages = new ArrayList<ChatMessage>();

	public ChatConversation() {

	}

	public ChatConversation(ChatConversation other) {
		this.id = other.id;
		this.participants = other.participants;
		this.messages = other.messages;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<EmissaryUser> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<EmissaryUser> participants) {
		this.participants = participants;
	}

	public void addParticipant(EmissaryUser participant) {
		this.participants.add(participant);
	}

	public List<ChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatMessage> messages) {
		this.messages = messages;
	}

	public void addMessage(ChatMessage message) {
		this.messages.add(message);
	}

	public ChatMessage lastMessage() {
		assert(messages.size() > 0);
		return messages.get(messages.size() - 1);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}

		if(!(o instanceof ChatConversation)) {
			return false;
		}

		var that = (ChatConversation)o;
		return Objects.equals(this.id, that.id);
	}
}
