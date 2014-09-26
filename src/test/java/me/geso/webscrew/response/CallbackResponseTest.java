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

public class CallbackResponseTest {

	@Test
	public void testAddHeader() throws Exception {
		final HttpServlet servlet = new CallbackServlet(
				(req, resp) -> {
					final CallbackResponse r = new CallbackResponse(
							res -> resp.getWriter().write("Hi"));
					r.addHeader("X-Foo", "a");
					r.addHeader("X-Foo", "b");
					r.addHeader("X-Foo", "c");
					r.write(resp);
				});
		try (MechJettyServlet mech = new MechJettyServlet(servlet)) {
			mech.disableRedirectHandling();
			mech.addRequestListener(new PrintRequestListener());
			try (MechResponse res = mech.get("/").execute()) {
				assertThat(res.getStatusCode(), is(200));
				assertThat(res.getHeaders("X-Foo"),
						is(Arrays.asList("a", "b", "c")));
			}
		}
	}

	@Test
	public void testAddCookie() throws Exception {
		final HttpServlet servlet = new CallbackServlet(
				(req, resp) -> {
					final CallbackResponse r = new CallbackResponse(
							res -> resp.getWriter().write("Hi"));
					r.addCookie(new Cookie("a", "B"));
					r.write(resp);
				});
		try (MechJettyServlet mech = new MechJettyServlet(servlet)) {
			mech.disableRedirectHandling();
			mech.addRequestListener(new PrintRequestListener());
			try (MechResponse res = mech.get("/").execute()) {
				assertThat(res.getStatusCode(), is(200));
				assertThat(res.getHeaders("Set-Cookie"),
						is(Arrays.asList("a=B")));
			}
		}
	}


}
