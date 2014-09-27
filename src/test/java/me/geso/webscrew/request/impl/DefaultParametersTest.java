package me.geso.webscrew.request.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import me.geso.webscrew.Parameters;

import org.junit.Test;

public class DefaultParametersTest {

	@Test
	public void testGetOK() {
		final Parameters parameters = DefaultParameters.builder()
				.put("hoge", "fuga")
				.build();
		assertThat(parameters.getFirst("hoge").get(), is("fuga"));
		assertThat(parameters.getFirst("nothing").isPresent(), is(false));
	}

	@Test
	public void testGetAll() {
		final Parameters parameters = DefaultParameters.builder()
				.put("hoge", "fuga")
				.put("hoge", "hige")
				.build();
		assertThat(parameters.getAll("hoge"),
				is(Arrays.asList("fuga", "hige")));
		assertThat(parameters.getAll("nothing").size(), is(0));
	}
}
