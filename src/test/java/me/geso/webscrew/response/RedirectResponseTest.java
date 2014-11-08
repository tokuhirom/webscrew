package me.geso.webscrew.response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import me.geso.webscrew.Utils;

import org.apache.http.HttpResponse;
import org.junit.Test;

public class RedirectResponseTest {

	@Test
	public void testAddHeader() throws Exception {
		Utils.test(
				(req, resp) -> {
					final RedirectResponse r = new RedirectResponse("/a");
					r.addHeader("X-Foo", "a");
					r.addHeader("X-Foo", "b");
					r.addHeader("X-Foo", "c");
					r.write(resp);
				},
				(mech) -> {
					mech.getMech2().getHttpClientBuilder()
							.disableRedirectHandling();
					HttpResponse res = mech.get("/").execute().getResponse();
					assertThat(res.getStatusLine().getStatusCode(), is(302));
					assertThat(
							Arrays.stream(res.getHeaders("X-Foo"))
									.map(it -> it.getValue())
									.collect(Collectors.toList()),
							is(Arrays.asList("a", "b", "c")));
				});
	}

	@Test
	public void testAddCookie() throws Exception {
		Utils.test(
				(req, resp) -> {
					final RedirectResponse r = new RedirectResponse("/a");
					r.addCookie(new Cookie("a", "B"));
					r.write(resp);
				},
				(mech) -> {
					mech.getMech2().getHttpClientBuilder()
							.disableRedirectHandling();
					HttpResponse res = mech.get("/").execute().getResponse();
					assertThat(res.getStatusLine().getStatusCode(), is(302));
					assertThat(
							Arrays.stream(res.getHeaders("Set-Cookie"))
									.map(it -> it.getValue())
									.collect(Collectors.toList()),
							is(Arrays.asList("a=B")));
				});
	}

}
