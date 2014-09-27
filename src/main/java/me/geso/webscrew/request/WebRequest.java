package me.geso.webscrew.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import me.geso.webscrew.Parameters;


public interface WebRequest {

	/**
	 * Get PATH_INFO.
	 * 
	 * @return
	 */
	public String getPathInfo();

	/**
	 * Get header string.
	 * 
	 * @param name
	 * @return
	 */
	public String getHeader(String name);

	/**
	 * Get all header values by name.
	 * 
	 * @param name
	 * @return
	 */
	public List<String> getHeaders(String name);

	/**
	 * Get all headers in Map.
	 * 
	 * @return
	 */
	public Map<String, List<String>> getHeaderMap();

	/**
	 * Get CONTENT_LENGTH.
	 * 
	 * @return
	 */
	public int getContentLength();

	/**
	 * Get HTTP_METHOD
	 * 
	 * @return
	 */
	public String getMethod();

	/**
	 * Returns the part of this request's URL from the protocol name up to the
	 * query string in the first line of the HTTP request. The web container
	 * does not decode this String. For example:
	 * 
	 * <table summary="Examples of Returned Values">
	 * <tr align=left>
	 * <th>First line of HTTP request</th>
	 * <th>Returned Value</th>
	 * <tr>
	 * <td>POST /some/path.html HTTP/1.1
	 * <td>
	 * <td>/some/path.html
	 * <tr>
	 * <td>GET http://foo.bar/a.html HTTP/1.0
	 * <td>
	 * <td>/a.html
	 * <tr>
	 * <td>HEAD /xyz?a=b HTTP/1.1
	 * <td>
	 * <td>/xyz
	 * </table>
	 *
	 * <p>
	 *
	 * @return a <code>String</code> containing the part of the URL from the
	 *         protocol name up to the query string
	 */
	public String getRequestURI();

	/**
	 * Get QUERY_STRING.
	 * 
	 * @return
	 */
	public String getQueryString();

	/**
	 * Get cooies.
	 * 
	 * @return
	 */
	public Cookie[] getCookies();

	/**
	 * Get session object.
	 * 
	 * @return
	 */
	public HttpSession getSession();

	/**
	 * Change session id.
	 */
	public void changeSessionId();

	/**
	 * Get uploaded file object by name.
	 * 
	 * @param name
	 * @return
	 */
	public Optional<WebRequestUpload> getFirstFileItem(final String name);

	/**
	 * Get uploaded file items by name.
	 * 
	 * @param name
	 * @return
	 */
	public List<WebRequestUpload> getAllFileItem(String parameterName);

	public Set<String> getFileItemNames();

	/**
	 * Get {@code InputStream} for content body.
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;

	public Parameters getQueryParams();

	public Parameters getBodyParams();


}