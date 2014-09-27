package me.geso.webscrew.request.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.geso.webscrew.Parameters;
import me.geso.webscrew.request.WebRequest;
import me.geso.webscrew.request.WebRequestUpload;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 * This class represents HTTP request. The object isn't thread safe. You
 * shouldn't share this object between threads.
 * 
 * @author tokuhirom
 *
 */
public class DefaultWebRequest implements WebRequest {
	private final HttpServletRequest servletRequest;
	private MultiMap<String, WebRequestUpload> uploads;
	private Parameters queryParams;
	private Parameters bodyParams;

	public DefaultWebRequest(final HttpServletRequest request,
			String characterEncoding) throws UnsupportedEncodingException {
		this.servletRequest = request;
		this.servletRequest.setCharacterEncoding(characterEncoding);
	}

	public DefaultWebRequest(final HttpServletRequest request,
			Charset characterEncoding) throws UnsupportedEncodingException {
		this(request, characterEncoding.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getPathInfo()
	 */
	@Override
	public String getPathInfo() {
		return this.servletRequest.getPathInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(final String name) {
		return this.servletRequest.getHeader(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getHeaders(java.lang.String)
	 */
	@Override
	public List<String> getHeaders(final String name) {
		return Collections.list(this.servletRequest.getHeaders(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getHeaderMap()
	 */
	@Override
	public Map<String, List<String>> getHeaderMap() {
		final Map<String, List<String>> map = new TreeMap<>();
		final Enumeration<String> headerNames = this.servletRequest
				.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			final String name = headerNames.nextElement();
			final ArrayList<String> values = Collections
					.list(this.servletRequest
							.getHeaders(name));
			map.put(name, values);
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getContentLength()
	 */
	@Override
	public int getContentLength() {
		return this.servletRequest.getContentLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getMethod()
	 */
	@Override
	public String getMethod() {
		return this.servletRequest.getMethod();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getRequestURI()
	 */
	@Override
	public String getRequestURI() {
		return this.servletRequest.getRequestURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getQueryString()
	 */
	@Override
	public String getQueryString() {
		return this.servletRequest.getQueryString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getCookies()
	 */
	@Override
	public Cookie[] getCookies() {
		return this.servletRequest.getCookies();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getSession()
	 */
	@Override
	public HttpSession getSession() {
		return this.servletRequest.getSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#changeSessionId()
	 */
	@Override
	public void changeSessionId() {
		this.servletRequest.changeSessionId();
	}

	/**
	 * Get character encoding.
	 * 
	 * @return
	 */
	public String getCharacterEncoding() {
		final String encoding = this.servletRequest.getCharacterEncoding();
		if (encoding == null) {
			throw new IllegalStateException(
					"HttpServletRequest#getCharacterEncoding() returns null");
		}
		return encoding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getFileItem(java.lang.String)
	 */
	@Override
	public Optional<WebRequestUpload> getFileItem(final String name) {
		@SuppressWarnings("unchecked")
		final Collection<WebRequestUpload> items = (Collection<WebRequestUpload>) this
				.getFileItemMap().get(name);
		if (items == null) {
			return Optional.empty();
		}
		return items.stream().findFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getFileItems(java.lang.String)
	 */
	@Override
	public Collection<WebRequestUpload> getFileItems(final String name) {
		@SuppressWarnings("unchecked")
		final Collection<WebRequestUpload> items = (Collection<WebRequestUpload>) this
				.getFileItemMap().get(name);
		if (items == null) {
			return new ArrayList<>();
		}
		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getFileItemMap()
	 */
	@Override
	public MultiMap<String, WebRequestUpload> getFileItemMap() {
		this.getBodyParams(); // initialize this.uploads
		return this.uploads;
	}

	/**
	 * Create new ServletFileUpload instance. You can override this method.
	 *
	 * <p>
	 * See also commons-fileupload.
	 * </p>
	 * 
	 * @return
	 */
	protected ServletFileUpload createServletFileUpload() {
		final FileItemFactory fileItemFactory = new DiskFileItemFactory();
		return new ServletFileUpload(fileItemFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getQueryParams()
	 */
	@Override
	public Parameters getQueryParams() {
		try {
			if (this.queryParams == null) {
				this.queryParams = UrlEncoded.parseQueryString(
						this.getQueryString(), this.getCharacterEncoding());
			}
			return this.queryParams;
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.geso.webscrew.request.WebRequest#getBodyParams()
	 */
	@Override
	public Parameters getBodyParams() {
		try {
			if (this.bodyParams == null) {
				if (this.servletRequest.getContentType().startsWith(
						"application/x-www-form-urlencoded")) {
					// application/x-www-form-urlencoded
					final String queryString = IOUtils.toString(
							this.servletRequest.getInputStream(),
							this.getCharacterEncoding());
					this.bodyParams = UrlEncoded.parseQueryString(
							queryString, this.getCharacterEncoding());
				} else if (ServletFileUpload
						.isMultipartContent(this.servletRequest)) {
					// multipart/form-data
					final MultiMap<String, String> bodyParams = new MultiValueMap<String, String>();
					final MultiMap<String, WebRequestUpload> uploads = new MultiValueMap<>();
					final ServletFileUpload servletFileUpload = this
							.createServletFileUpload();
					final List<FileItem> fileItems = servletFileUpload
							.parseRequest(this.servletRequest);
					for (final FileItem fileItem : fileItems) {
						if (fileItem.isFormField()) {
							final String value = fileItem.getString(this
									.getCharacterEncoding());
							bodyParams.put(fileItem.getFieldName(), value);
						} else {
							uploads.put(fileItem.getFieldName(),
									new DefaultWebRequestUpload(fileItem));
						}
					}
					this.uploads = uploads;
					this.bodyParams = new DefaultParameters(bodyParams);
				}
			}
			return this.bodyParams;
		} catch (final IOException | FileUploadException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.servletRequest.getInputStream();
	}

}
