package emissarybackend;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class EmissaryUser {
	private @Id @GeneratedValue Long id;
	
	private String name;

	@ManyToMany(mappedBy = "participants")
	Set<ChatConversation> conversations = new HashSet<ChatConversation>();

	public EmissaryUser() {
		name = "";
	}

	public EmissaryUser(EmissaryUser other) {
		this.name = other.name;
		this.id = other.id;
		this.conversations = other.conversations;
	}

	public EmissaryUser(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<ChatConversation> getConversations() {
		return conversations;
	}

	public void setConversations(Set<ChatConversation> conversations) {
		this.conversations = conversations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.id);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}

		if(!(o instanceof EmissaryUser)) {
			return false;
		}

		var that = (EmissaryUser)o;
		return Objects.equals(this.name, that.name)
			&& Objects.equals(this.id, that.id);
	}
}
