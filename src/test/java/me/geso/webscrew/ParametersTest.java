package me.geso.webscrew;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.Test;

public class ParametersTest {

	@Test
	public void testGetOK() {
		final MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "fuga");
		final Parameters parameters = new Parameters(mm);
		assertThat(parameters.getFirst("hoge").get(), is("fuga"));
		assertThat(parameters.getFirst("nothing").isPresent(), is(false));
	}

	@Test
	public void testGetAll() {
		final MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "fuga");
		mm.put("hoge", "hige");
		final Parameters parameters = new Parameters(mm);
		assertThat(parameters.getAll("hoge"),
				is(Arrays.asList("fuga", "hige")));
		assertThat(parameters.getAll("nothing").size(), is(0));
	}
}
