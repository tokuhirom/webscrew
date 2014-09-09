package me.geso.webscrew;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class HeadersTest {

	@Test
	public void test() {
		Headers headers = new Headers();
		headers.add("hoge", "fuga");
		headers.add("hoge", "hige");
		List<String> all = headers.getAll("hoge");
		assertEquals(2, all.size());
		assertEquals("fuga", all.get(0));
		assertEquals("hige", all.get(1));
	}

	// header key should not include new line.
	@Test
	public void testKeyContainsNewLine() {
		Headers headers = new Headers();
		RuntimeException gotException = null;
		try {
			headers.add("ho\nge", "fuga");
		} catch (RuntimeException e) {
			gotException = e;
		}
		assertTrue(gotException != null);
		assertEquals("You can't include new line character in header key.",
				gotException.getMessage());
	}

	// header key should not include new line.
	@Test
	public void testValueContainsNewLine() {
		Headers headers = new Headers();
		RuntimeException gotException = null;
		try {
			headers.add("hoge", "fu\nga");
		} catch (RuntimeException e) {
			gotException = e;
		}
		assertTrue(gotException != null);
		assertEquals("You can't include new line character in header value.",
				gotException.getMessage());
	}

	@Test
	public void testHeadersNames() {
		Headers headers = new Headers();
		headers.add("hoge", "fuga");
		headers.add("foo", "bar");
		Set<String> headerNames = headers.headerNames();
		assertEquals("foo,hoge",
				headerNames.stream().sorted().collect(Collectors.joining(",")));
	}
}
