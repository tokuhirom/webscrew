package me.geso.webscrew;

import java.util.List;
import java.util.Optional;

/**
 * There is no reason to use this.
 */
@Deprecated
public interface Parameters {

	public Optional<String> getFirst(final String name);

	public List<String> getAll(final String name);

}