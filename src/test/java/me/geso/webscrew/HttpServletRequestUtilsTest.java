package me.geso.webscrew;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.geso.mech2.Mech2;
import me.geso.mech2.Mech2Result;
import me.geso.mech2.Mech2WithBase;
import me.geso.servlettester.jetty.JettyServletTester;

public class HttpServletRequestUtilsTest {

	@Test
	public void testGetCurrentURL() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setStatus(200);
			resp.getWriter().write(HttpServletRequestUtils.getCurrentURL(req).toString());
		}, uri -> {
			final Mech2WithBase mech2WithBase = new Mech2WithBase(Mech2.builder().build(), uri);
			final Mech2Result result = mech2WithBase.get("/foo/bar")
					.addQueryParameter("X", "Y")
					.execute();
			assertThat(result.getResponseBodyAsString().matches("http://127.0.0.1:\\d+/foo/bar\\?X=Y"), is(true));
		});
	}

	@Test
	public void testUriFor() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setStatus(200);
			final URIForTestCase form = new ObjectMapper().readValue(req.getInputStream(), URIForTestCase.class);
			try {
				resp.getWriter().write(HttpServletRequestUtils.uriFor(req, form.getPath(), form.getParameters()).toString());
			} catch (URISyntaxException e) {
				fail();
				throw new RuntimeException(e);
			}
		}, uri -> {
			final Mech2WithBase mech2WithBase = new Mech2WithBase(Mech2.builder().build(), uri);
			final URIForTestCase form = new URIForTestCase();
			form.setPath("/");
			form.setParameters(ImmutableMap.<String, String>builder()
					.put("oh", "my")
					.build());
			final Mech2Result result = mech2WithBase.postJSON("/foo/bar", form)
					.addQueryParameter("X", "Y")
					.execute();
			System.out.println(result.getResponseBodyAsString());
			assertThat(result.getResponseBodyAsString().matches("http://127.0.0.1:\\d+/\\?oh=my"), is(true));
		});

		JettyServletTester.runServlet((req, resp) -> {
			resp.setStatus(200);
			final URIForTestCase form = new ObjectMapper().readValue(req.getInputStream(), URIForTestCase.class);
			try {
				resp.getWriter().write(HttpServletRequestUtils.uriFor(req, form.getPath(), form.getParameters()).toString());
			} catch (URISyntaxException e) {
				fail();
				throw new RuntimeException(e);
			}
		}, uri -> {
			final Mech2WithBase mech2WithBase = new Mech2WithBase(Mech2.builder().build(), uri);
			final URIForTestCase form = new URIForTestCase();
			form.setPath("/goose");
			form.setParameters(ImmutableMap.<String, String>builder()
					.put("oh", "my")
					.put("com", "bu")
					.build());
			final Mech2Result result = mech2WithBase.postJSON("/foo/bar", form)
					.addQueryParameter("X", "Y")
					.execute();
			System.out.println(result.getResponseBodyAsString());
			assertThat(result.getResponseBodyAsString().matches("http://127.0.0.1:\\d+/goose\\?oh=my&com=bu"), is(true));
		});
	}

	@Data
	public static class URIForTestCase {
		private String path;
		private Map<String, String> parameters;
	}

	@Test
	public void testUriWith() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.setStatus(200);
			final URIWithTestCase form = new ObjectMapper().readValue(req.getInputStream(), URIWithTestCase.class);
			try {
				resp.getWriter().write(HttpServletRequestUtils.uriWith(req, form.getParameters()).toString());
			} catch (URISyntaxException e) {
				fail();
				throw new RuntimeException(e);
			}
		}, uri -> {
			final Mech2WithBase mech2WithBase = new Mech2WithBase(Mech2.builder().build(), uri);
			final URIWithTestCase form = new URIWithTestCase();
			form.setParameters(ImmutableMap.<String, String>builder()
					.put("oh", "my")
					.put("com", "bu")
					.build());
			final Mech2Result result = mech2WithBase.postJSON("/foo/bar", form)
					.addQueryParameter("X", "Y")
					.execute();
			System.out.println(result.getResponseBodyAsString());
			assertThat(result.getResponseBodyAsString().matches("http://127.0.0.1:\\d+/foo/bar\\?X=Y&oh=my&com=bu"), is(true));
		});
		JettyServletTester.runServlet((req, resp) -> {
			resp.setStatus(200);
			final URIWithTestCase form = new ObjectMapper().readValue(req.getInputStream(), URIWithTestCase.class);
			try {
				resp.getWriter().write(HttpServletRequestUtils.uriWith(req, form.getParameters()).toString());
			} catch (URISyntaxException e) {
				fail();
				throw new RuntimeException(e);
			}
		}, uri -> {
			final Mech2WithBase mech2WithBase = new Mech2WithBase(Mech2.builder().build(), uri);
			final URIWithTestCase form = new URIWithTestCase();
			form.setParameters(ImmutableMap.<String, String>builder()
					.put("X", "Z")
					.build());
			final Mech2Result result = mech2WithBase.postJSON("/foo/bar", form)
					.addQueryParameter("X", "Y")
					.execute();
			System.out.println(result.getResponseBodyAsString());
			assertThat(result.getResponseBodyAsString().matches("http://127.0.0.1:\\d+/foo/bar\\?X=Z"), is(true));
		});
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class URIWithTestCase {
		private Map<String, String> parameters;
	}
}
