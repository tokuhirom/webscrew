package me.geso.webscrew.request.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The class represents paremeters.
 * 
 * @author tokuhirom
 *
 */
public class DefaultParameters {
	@Override
	public String toString() {
		return "Parameters [map=" + map + "]";
	}

	private final Map<String, List<String>> map;

	private DefaultParameters(Map<String, List<String>> map) {
		this.map = map;
	}

	/**
	 * Get a path parameter in String.
	 * 
	 * @param name
	 * @return
	 */
	public Optional<String> getFirst(String name) {
		final Collection<String> collection = map
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
	public List<String> getAll(final String name) {
		final List<String> collection = map
				.get(name);
		if (collection == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(collection);
		}
	}

	public Set<String> getKeys() {
		return this.map.keySet();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final Map<String, List<String>> map = new LinkedHashMap<>();

		private Builder() {
		}

		public Builder put(String key, String value) {
			if (map.containsKey(key)) {
				final List<String> list = map.get(key);
				list.add(value);
				map.put(key, list);
			} else {
				final ArrayList<String> list = new ArrayList<>();
				list.add(value);
				map.put(key, list);
			}
			return this;
		}

		public DefaultParameters build() {
			return new DefaultParameters(map);
		}
	}
}
