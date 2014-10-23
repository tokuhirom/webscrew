package me.geso.webscrew.request.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

public class HttpBasicAuthHeaderParserTest {

	@Test
	public void test() {
		{
			Optional<HttpBasicAuthUserPass> parse = HttpBasicAuthHeaderParser
					.parse(null);
			assertFalse(parse.isPresent());
		}

		{
			Optional<HttpBasicAuthUserPass> parse = HttpBasicAuthHeaderParser
					.parse("Basic YWRtaW46czNjcjN0");
			assertTrue(parse.isPresent());
			assertEquals(parse.get().getUserId(), "admin");
			assertEquals(parse.get().getPassword(), "s3cr3t");
		}
	}

	@Test
	public void testBadBase64() {
		Optional<HttpBasicAuthUserPass> parse = HttpBasicAuthHeaderParser
				.parse("Basic bogus");
		assertEquals(false, parse.isPresent());
	}
}
