package me.geso.webscrew.request.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
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
					assertThat(r.getQueryParams().getFirst("hoge").get(),
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

	@Test
	public void testGetFirstFileItem() throws Exception {
		final HttpServlet servlet = new CallbackServlet(
				(req, resp) -> {
					final DefaultWebRequest r = new DefaultWebRequest(req,
							StandardCharsets.UTF_8);
					assertThat(
							r.getFirstFileItem("hoge").isPresent(),
							is(true));
					assertThat(
							r.getFirstFileItem("hoge").get().getString("UTF-8"),
							is("hello"));
					assertThat(
							r.getFirstFileItem("fuga").isPresent(),
							is(false));
					resp.getWriter().write("ok");
				});
		try (MechJettyServlet mech = new MechJettyServlet(servlet)) {
			try (MechResponse res = mech.postMultipart("/")
					.file("hoge", new File("src/test/resources/hello.txt"))
					.execute()) {
				assertThat(res.getContentString(),
						is("ok"));
			}
		}
	}

	@Test
	public void testGetUserAgent() throws Exception {
		final HttpServlet servlet = new CallbackServlet(
				(req, resp) -> {
					final DefaultWebRequest r = new DefaultWebRequest(req,
							StandardCharsets.UTF_8);
					assertThat(
							r.getUserAgent().startsWith("Apache-HttpClient"),
							is(true));
					resp.getWriter().write("ok");
				});
		try (MechJettyServlet mech = new MechJettyServlet(servlet)) {
			try (MechResponse res = mech.get("/").execute()) {
				assertThat(res.getStatusCode(),
						is(200));
				assertThat(res.getContentString(),
						is("ok"));
			}
		}
	}

}
