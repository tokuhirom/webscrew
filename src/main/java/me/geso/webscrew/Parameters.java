package me.geso.webscrew;

import java.util.Collection;
import java.util.Optional;

public interface Parameters {

	public Optional<String> getFirst(String name);

	public Collection<String> getAll(String name);

}