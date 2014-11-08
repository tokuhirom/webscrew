package me.geso.webscrew.response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import me.geso.webscrew.Utils;

import org.apache.http.HttpResponse;
import org.junit.Test;

public class ByteArrayResponseTest {

	@Test
	public void testAddHeaders() throws Exception {
		Utils.test((req, resp) -> {
			final ByteArrayResponse byteArrayResponse = new ByteArrayResponse(
					200, "".getBytes());
			byteArrayResponse.addHeader("X-Foo", "a");
			byteArrayResponse.addHeader("X-Foo", "b");
			byteArrayResponse.addHeader("X-Foo", "c");
			byteArrayResponse.write(resp);
		}, (mech) -> {
			HttpResponse res = mech.get("/")
					.execute()
					.getResponse();
			assertThat(res.getStatusLine().getStatusCode(), is(200));
					assertThat(
							Arrays.stream(res.getHeaders("X-Foo"))
									.map(it -> it.getValue())
									.collect(Collectors.toList()),
					is(Arrays.asList("a", "b", "c")));
		});
	}

	@Test
	public void testAddCookie() throws Exception {
		Utils.test((req, resp) -> {
			final ByteArrayResponse byteArrayResponse = new ByteArrayResponse(
					200, "".getBytes());
			final Cookie cookie = new Cookie("a", "b");
			cookie.setMaxAge(500);
			cookie.setHttpOnly(true);
			byteArrayResponse.addCookie(cookie);
			byteArrayResponse.addCookie(new Cookie("c", "d"));
			byteArrayResponse.write(resp);
		}, (mech) -> {
			HttpResponse res = mech.get("/").execute().getResponse();
			assertThat(
					res.getHeaders("Set-Cookie").length,
					is(2));
			assertThat(
					res.getHeaders("Set-Cookie")[0].getValue()
							.matches(
									"a=b;Expires=.* GMT;HttpOnly"),
					is(true));
			assertThat(
					res.getHeaders("Set-Cookie")[1].getValue()
							.matches(
									"c=d"),
					is(true));
		});
	}

	@Test
	public void testGetBytes() {
		final byte[] body = "hoge".getBytes();
		final ByteArrayResponse resp = new ByteArrayResponse(200, body);
		assertThat(resp.getBody(), is(body));
	}
}
