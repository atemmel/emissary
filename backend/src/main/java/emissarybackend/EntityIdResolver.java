package emissarybackend;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import javax.persistence.EntityManager;

public class EntityIdResolver implements ObjectIdResolver {
	final private EntityManager manager;

	public EntityIdResolver(final EntityManager manager) {
		this.manager = manager;
	}

	@Override
	public void bindItem(
			final ObjectIdGenerator.IdKey id,
			final Object pojo) {
		
	}

	@Override
	public Object resolveId(final ObjectIdGenerator.IdKey id) {
		return manager.find(id.scope, id.key);
	}

	@Override
	public ObjectIdResolver newForDeserialization(final Object context) {
		return this;
	}

	@Override
	public boolean canUseFor(final ObjectIdResolver resolverType) {
		return false;
	}
}
