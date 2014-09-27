package me.geso.webscrew.request;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import me.geso.webscrew.Parameters;

import org.apache.commons.collections4.MultiMap;

public interface WebRequest {

	/**
	 * Get PATH_INFO.
	 * 
	 * @return
	 */
	public abstract String getPathInfo();

	/**
	 * Get header string.
	 * 
	 * @param name
	 * @return
	 */
	public abstract String getHeader(String name);

	/**
	 * Get all header values by name.
	 * 
	 * @param name
	 * @return
	 */
	public abstract List<String> getHeaders(String name);

	/**
	 * Get all headers in Map.
	 * 
	 * @return
	 */
	public abstract Map<String, List<String>> getHeaderMap();

	/**
	 * Get CONTENT_LENGTH.
	 * 
	 * @return
	 */
	public abstract int getContentLength();

	/**
	 * Get HTTP_METHOD
	 * 
	 * @return
	 */
	public abstract String getMethod();

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
	public abstract String getRequestURI();

	/**
	 * Get QUERY_STRING.
	 * 
	 * @return
	 */
	public abstract String getQueryString();

	/**
	 * Get cooies.
	 * 
	 * @return
	 */
	public abstract Cookie[] getCookies();

	/**
	 * Get session object.
	 * 
	 * @return
	 */
	public abstract HttpSession getSession();

	/**
	 * Change session id.
	 */
	public abstract void changeSessionId();

	/**
	 * Get uploaded file object by name.
	 * 
	 * @param name
	 * @return
	 */
	public abstract Optional<WebRequestUpload> getFileItem(String name);

	/**
	 * Get uploaded file items by name.
	 * 
	 * @param name
	 * @return
	 */
	public abstract Collection<WebRequestUpload> getFileItems(String name);

	/**
	 * Get all uploaded file items.
	 * 
	 * @return
	 */
	public abstract MultiMap<String, WebRequestUpload> getFileItemMap();

	public abstract Parameters getQueryParams();

	public abstract Parameters getBodyParams();

}