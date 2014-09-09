package me.geso.webscrew;

import static org.junit.Assert.*;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.Test;

public class ParametersTest {

	@Test
	public void testGetOK() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "fuga");
		Parameters parameters = new Parameters(mm);
		assertEquals("fuga", parameters.get("hoge"));
	}

	@Test
	public void testGetFail() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		Parameters parameters = new Parameters(mm);
		RuntimeException gotException = null;
		try {
			parameters.get("hoge");
		} catch (RuntimeException e) {
			gotException = e;
		}
		assertEquals("Missing mandatory parameter: hoge", gotException.getMessage());
	}

	@Test
	public void testGetLongOK() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "5963");
		Parameters parameters = new Parameters(mm);
		assertEquals(5963L, parameters.getLong("hoge"));
	}

	@Test
	public void testGetIntOK() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "5963");
		Parameters parameters = new Parameters(mm);
		assertEquals(5963L, parameters.getInt("hoge"));
	}

	@Test
	public void testGetDoubleOK() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "3.14");
		Parameters parameters = new Parameters(mm);
		assertEquals(3.14, parameters.getDouble("hoge"), 0.01);
	}

	@Test
	public void testGetOptionalLongOK() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "5963");
		Parameters parameters = new Parameters(mm);
		assertEquals(5963L, parameters.getOptionalLong("hoge").getAsLong());
	}

	@Test
	public void testGetOptionalLongEmpty() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		Parameters parameters = new Parameters(mm);
		assertFalse(parameters.getOptionalLong("hoge").isPresent());
	}

	@Test
	public void testGetOptionalIntOK() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "5963");
		Parameters parameters = new Parameters(mm);
		assertEquals(5963L, parameters.getOptionalInt("hoge").getAsInt());
	}

	@Test
	public void testGetOptionalIntEmpty() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		Parameters parameters = new Parameters(mm);
		assertFalse(parameters.getOptionalInt("hoge").isPresent());
	}

	@Test
	public void testGetOptionalDoubleOK() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "5963");
		Parameters parameters = new Parameters(mm);
		assertEquals(5963L, parameters.getOptionalDouble("hoge").getAsDouble(), 0.01);
	}

	@Test
	public void testGetOptionalDoubleEmpty() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		Parameters parameters = new Parameters(mm);
		assertFalse(parameters.getOptionalDouble("hoge").isPresent());
	}

	@Test
	public void testGetOptionalOk() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		mm.put("hoge", "fuga");
		Parameters parameters = new Parameters(mm);
		assertEquals("fuga", parameters.getOptional("hoge").get());
	}

	@Test
	public void testGetOptionalEmpty() {
		MultiMap<String, String> mm = new MultiValueMap<>();
		Parameters parameters = new Parameters(mm);
		assertFalse(parameters.getOptional("hoge").isPresent());
	}

}
