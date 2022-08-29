package emissarybackend;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;

@Entity
public class ChatMessageAttachment {

	private @Id @GeneratedValue Long id;

	@Lob
	@Column(name="bytes_column", columnDefinition="BLOB")
	private byte[] bytes;
	private String name;
	private String type;
	
	@ElementCollection
	@CollectionTable(name = "chat_polls", joinColumns = @JoinColumn(name = "poll_id"))
	@MapKeyColumn(name = "field_key")
	@Column(name = "field_val")
	private Map<String, Integer> poll = new HashMap<String, Integer>();

	public ChatMessageAttachment() {
	}

	public ChatMessageAttachment(
			byte[] bytes,
			String name,
			String type) {
		this.bytes = bytes;
		this.name = name;
		this.type = type;
	}

	public ChatMessageAttachment(HashMap<String, Integer> poll) {
		this.poll = poll;
	}

	public ChatMessageAttachment(ChatMessageAttachment other) {
		this.id = other.id;
		this.bytes = other.bytes;
		this.name = other.name;
		this.type = other.type;
		this.poll = other.poll;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Integer> getPoll() {
		return poll;
	}

	public void setPoll(Map<String, Integer> poll) {
		this.poll = poll;
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

		if(!(o instanceof ChatMessageAttachment)) {
			return false;
		}

		var that = (ChatMessageAttachment)o;
		return Objects.equals(this.name, that.name)
			&& Objects.equals(this.id, that.id)
			&& Objects.equals(this.type, that.type);
	}
}

