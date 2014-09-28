package me.geso.webscrew;

import java.util.List;
import java.util.Optional;

public interface Parameters {

	public Optional<String> getFirst(final String name);

	public List<String> getAll(final String name);

}