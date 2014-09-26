package me.geso.webscrew.response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;

import me.geso.mech.MechJettyServlet;
import me.geso.mech.MechResponse;
import me.geso.mech.PrintRequestListener;
import me.geso.webscrew.CallbackServlet;

import org.junit.Test;

public class ByteArrayResponseTest {

	@Test
	public void testAddHeaders() throws Exception {
		final HttpServlet servlet = new CallbackServlet(
				(req, resp) -> {
					final ByteArrayResponse byteArrayResponse = new ByteArrayResponse();
					byteArrayResponse.addHeader("X-Foo", "a");
					byteArrayResponse.addHeader("X-Foo", "b");
					byteArrayResponse.addHeader("X-Foo", "c");
					byteArrayResponse.write(resp);
				});
		try (MechJettyServlet mech = new MechJettyServlet(servlet)) {
			try (MechResponse res = mech.get("/").execute()) {
				assertThat(res.getHeaders("X-Foo"),
						is(Arrays.asList("a", "b", "c")));
			}
		}
	}

	@Test
	public void testAddCookie() throws Exception {
		final HttpServlet servlet = new CallbackServlet(
				(req, resp) -> {
					final ByteArrayResponse byteArrayResponse = new ByteArrayResponse();
					final Cookie cookie = new Cookie("a", "b");
					cookie.setMaxAge(500);
					cookie.setHttpOnly(true);
					byteArrayResponse.addCookie(cookie);
					byteArrayResponse.addCookie(new Cookie("c", "d"));
					byteArrayResponse.write(resp);
				});
		try (MechJettyServlet mech = new MechJettyServlet(servlet)) {
			mech.addRequestListener(new PrintRequestListener());
			try (MechResponse res = mech.get("/").execute()) {
				assertThat(
						res.getHeaders("Set-Cookie")
								.size(),
						is(2));
				assertThat(
						res.getHeaders("Set-Cookie")
								.get(0)
								.matches(
										"a=b;Expires=.* GMT;HttpOnly"),
						is(true));
				assertThat(
						res.getHeaders("Set-Cookie")
								.get(1)
								.matches(
										"c=d"),
						is(true));
			}
		}
	}
}
