package me.geso.webscrew;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;

import lombok.NonNull;

/**
 * [EXPERIMENTAL]
 * This class contains utility functions for HttpServletRequest.
 */
public class HttpServletRequestUtils {
	/**
	 * [EXPERIMENTAL] Get the URL for current HTTP request.
	 * Return value includes query string.
	 *
	 * @return Reconstructed URL
	 * @throws java.net.MalformedURLException
	 */
	public static URL getCurrentURL(@NonNull final HttpServletRequest httpServletRequest) throws
			MalformedURLException {
		StringBuffer url = httpServletRequest.getRequestURL();
		final String queryString = httpServletRequest.getQueryString();
		if (queryString != null && !queryString.isEmpty()) {
			url.append("?").append(httpServletRequest.getQueryString());
		}
		return new URL(url.toString());
	}

	private static URIBuilder getRelativePath(@NonNull final HttpServletRequest httpServletRequest, @NonNull final String path) throws MalformedURLException,
			URISyntaxException {
		if (path.startsWith("/")) {
			return new URIBuilder().setScheme(httpServletRequest.getScheme())
					.setHost(httpServletRequest.getServerName())
					.setPort(httpServletRequest.getServerPort())
					.setPath(httpServletRequest.getContextPath() + path);
		} else {
			final StringBuffer requestURL = httpServletRequest.getRequestURL();
			return new URIBuilder(new URL(new URL(requestURL.toString()), path).toURI());
		}
	}

	/**
	 * [EXPERIMENTAL] Constructs an absolute URI object based on the application root, the provided path, and the additional arguments and query parameters provided.
	 * This method cares context path. You can use relative path from context root.
	 *
	 * For example, if your context path is {@code http://example.com/xxx/},
	 * <code>uriFor("/x")</code> returns {@code http://example.com/xxx/x}
	 *
	 * @param httpServletRequest instance of HttpServletRequest.
	 * @param path Path from the current URL. You can use root relative URL from context root.
	 * @param parameters Query parameters.
	 * @return Constructed URI.
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public static URI uriFor(@NonNull final HttpServletRequest httpServletRequest, @NonNull String path, @NonNull Map<String, String> parameters) throws URISyntaxException, MalformedURLException {
		final URIBuilder builder = HttpServletRequestUtils.getRelativePath(httpServletRequest, path);

		parameters.entrySet().stream()
				.forEach(it -> builder.setParameter(it.getKey(), it.getValue()));
		return builder.build();
	}

	/**
	 * [EXPERIMENTAL] Returns a rewritten URI object for the current request.
	 * Key/value pairs passed in will override existing parameters.
	 * You can remove an existing parameter by passing in an undef value.
	 * Unmodified pairs will be preserved.
	 *
	 * @param httpServletRequest instance of HttpServletRequest.
	 * @param parameters Query parameters.
	 * @return Constructed URI.
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public static URI uriWith(@NonNull final HttpServletRequest httpServletRequest, final Map<String, String> parameters) throws URISyntaxException, MalformedURLException {
		URIBuilder builder = new URIBuilder(String.valueOf(getCurrentURL(httpServletRequest)));
		parameters.entrySet().stream()
				.forEach(it -> builder.setParameter(it.getKey(), it.getValue()));
		return builder.build();
	}
}
