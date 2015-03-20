package me.geso.webscrew.request.auth;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

@SuppressWarnings("SpellCheckingInspection")
public class HttpBasicAuthUserPassTest {

	@Test
	public void test() {
		{
			HttpBasicAuthUserPass httpBasicAuth = new HttpBasicAuthUserPass(
					"admin", "s3cr3t");
			String asBasicAuthHeader = httpBasicAuth
					.asHeader(StandardCharsets.UTF_8);
			assertEquals(asBasicAuthHeader, "Basic YWRtaW46czNjcjN0");
		}
		{
			HttpBasicAuthUserPass httpBasicAuth = new HttpBasicAuthUserPass(
					"admin",
					"s3cr3tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
			String asBasicAuthHeader = httpBasicAuth
					.asHeader(StandardCharsets.UTF_8);
			assertEquals(
					asBasicAuthHeader,
					"Basic YWRtaW46czNjcjN0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHQ=");
		}
	}

}
