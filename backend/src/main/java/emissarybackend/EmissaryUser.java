package emissarybackend;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class EmissaryUser {
	private @Id @GeneratedValue Long id;
	
	private String name;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@ManyToMany(mappedBy = "participants", cascade = CascadeType.ALL)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	Set<ChatConversation> conversations = new HashSet<ChatConversation>();

	public EmissaryUser() {
		name = "";
		password = "";
	}

	public EmissaryUser(EmissaryUser other) {
		this.name = other.name;
		this.password = other.password;
		this.id = other.id;
		this.conversations = other.conversations;
	}

	public EmissaryUser(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

		EmissaryUser that = (EmissaryUser)o;
		return Objects.equals(this.name, that.name)
			&& Objects.equals(this.password, that.password)
			&& Objects.equals(this.id, that.id);
	}
}
