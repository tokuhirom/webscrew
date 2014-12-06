package me.geso.webscrew.request;

import java.io.InputStream;

/**
 * Use Part directly.
 */
@Deprecated
public interface WebRequestUpload {

	/**
	 * Get String representation from uploaded file.
	 * 
	 * @param encoding
	 * @return
	 */
	public String getString(String encoding);

	/**
	 * Get InputStream from uploaded file.
	 * 
	 * @return
	 */
	public InputStream getInputStream();

	/**
	 * Get name of the uploaded file.
	 * 
	 * @return
	 */
	public String getName();

}
