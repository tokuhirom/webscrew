package me.geso.webscrew.response;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Test;

import me.geso.webscrew.Utils;

public class CallbackResponseTest {

	@Test
	public void testAddHeader() throws Exception {
		Utils.test((req, resp) -> {
			final CallbackResponse r = new CallbackResponse(
					res -> resp.getWriter().write("Hi"));
			r.addHeader("X-Foo", "a");
			r.addHeader("X-Foo", "b");
			r.addHeader("X-Foo", "c");
			r.write(resp);
		}, (mech) -> {
			mech.getMech2().getHttpClientBuilder().disableRedirectHandling();
			HttpResponse res = mech.get("/").execute().getResponse();
			assertThat(res.getStatusLine().getStatusCode(), is(200));
			assertThat(
					Arrays.stream(res.getHeaders("X-Foo"))
							.map(Header::getValue)
							.collect(Collectors.toList()),
					is(Arrays.asList("a", "b", "c")));
		});
	}

	@Test
	public void testAddCookie() throws Exception {
		Utils.test(
				(req, resp) -> {

					final CallbackResponse r = new CallbackResponse(
							res -> resp.getWriter().write("Hi"));
					r.addCookie(new Cookie("a", "B"));
					r.write(resp);
				},
				(mech) -> {
					mech.getMech2().getHttpClientBuilder()
							.disableRedirectHandling();
					HttpResponse res = mech.get("/").execute().getResponse();

					assertThat(res.getStatusLine().getStatusCode(), is(200));
					assertThat(
							Arrays.stream(res.getHeaders("Set-Cookie"))
									.map(Header::getValue)
									.collect(Collectors.toList()),
							is(Arrays.asList("a=B")));
				});
	}

}
