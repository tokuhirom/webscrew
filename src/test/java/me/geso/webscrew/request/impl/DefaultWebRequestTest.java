package me.geso.webscrew.request.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServlet;

import me.geso.mech.MechJettyServlet;
import me.geso.mech.MechResponse;
import me.geso.webscrew.CallbackServlet;

import org.junit.Test;

public class DefaultWebRequestTest {

	@Test
	public void testGetQueryParams() throws Exception {
		final HttpServlet servlet = new CallbackServlet(
				(req, resp) -> {
					final DefaultWebRequest r = new DefaultWebRequest(req,
							StandardCharsets.UTF_8);
					assertThat(r.getFirstQueryParameter("hoge").get(),
							is("fuga"));
					resp.getWriter().write("ok");
				});
		try (MechJettyServlet mech = new MechJettyServlet(servlet)) {
			try (MechResponse res = mech.get("/?hoge=fuga").execute()) {
				assertThat(res.getContentString(),
						is("ok"));
			}
		}
	}

}
