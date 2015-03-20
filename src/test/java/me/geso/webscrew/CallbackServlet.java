package me.geso.webscrew;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ServletCallback callback;

	public CallbackServlet(ServletCallback callback) {
		this.callback = callback;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			this.callback.service(req, resp);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@FunctionalInterface
	public interface ServletCallback {
		@SuppressWarnings("RedundantThrows")
		public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
				throws Exception;
	}
}
