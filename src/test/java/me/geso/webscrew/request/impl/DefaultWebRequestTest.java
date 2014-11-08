package me.geso.webscrew.request.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.charset.StandardCharsets;

import me.geso.mech2.Mech2Result;
import me.geso.webscrew.Utils;

import org.junit.Test;

public class DefaultWebRequestTest {

	@Test
	public void testGetQueryParams() throws Exception {
		Utils.test((req, resp) -> {
			final DefaultWebRequest r = new DefaultWebRequest(req,
					StandardCharsets.UTF_8);
			assertThat(r.getQueryParams().getFirst("hoge").get(),
					is("fuga"));
			resp.getWriter().write("ok");
		}, (mech) -> {
			mech.getMech2().getHttpClientBuilder().disableRedirectHandling();
			String body = mech.get("/")
					.addQueryParameter("hoge", "fuga")
					.execute()
					.getResponseBodyAsString();
			assertThat(body,
					is("ok"));
		});
	}

	@Test
	public void testGetFirstFileItem() throws Exception {
		Utils.test(
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
				},
				(mech) -> {
					mech.getMech2().getHttpClientBuilder()
							.disableRedirectHandling();
					mech.post("/");
					Mech2Result res = mech
							.postMultipart("/")
							.addBinaryBody("hoge",
									new File("src/test/resources/hello.txt"))
							.execute();
					assertThat(res.getResponseBodyAsString(),
							is("ok"));
				});
	}

	@Test
	public void testGetUserAgent() throws Exception {
		Utils.test((req, resp) -> {
			final DefaultWebRequest r = new DefaultWebRequest(req,
					StandardCharsets.UTF_8);
			assertThat(
					r.getUserAgent().startsWith("Apache-HttpClient"),
					is(true));
			resp.getWriter().write("ok");
		}, (mech) -> {
			mech.getMech2().getHttpClientBuilder().disableRedirectHandling();
			Mech2Result res = mech.get("/").execute();
			assertThat(res.getResponse().getStatusLine().getStatusCode(),
					is(200));
			assertThat(res.getResponseBodyAsString(),
					is("ok"));
		});
	}

}
