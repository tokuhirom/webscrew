package me.geso.webscrew;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.apache.commons.collections4.MultiMap;

/**
 * The class represents paremeters.
 * 
 * @author tokuhirom
 *
 */
public class Parameters {
	@Override
	public String toString() {
		return "Parameters [map=" + map + "]";
	}

	private final MultiMap<String, String> map;

	public Parameters(MultiMap<String, String> map) {
		this.map = map;
	}

	/**
	 * Get a path parameter in String.
	 * 
	 * @param name
	 * @return
	 */
	public Optional<String> getFirst(String name) {
		@SuppressWarnings("unchecked")
		final Collection<String> collection = (Collection<String>) map
				.get(name);
		if (collection == null) {
			return Optional.empty();
		} else {
			return collection.stream().findFirst();
		}
	}

	/**
	 * Get a path parameter in String.
	 * 
	 * @param name
	 * @return
	 */
	public Collection<String> getAll(String name) {
		@SuppressWarnings("unchecked")
		final Collection<String> collection = (Collection<String>) map
				.get(name);
		if (collection == null) {
			return Collections.emptyList();
		} else {
			return collection;
		}
	}

}
