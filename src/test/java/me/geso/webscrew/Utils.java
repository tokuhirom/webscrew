package me.geso.webscrew;

import java.io.IOException;
import java.net.URI;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import me.geso.mech2.Mech2;
import me.geso.mech2.Mech2WithBase;
import me.geso.servlettester.jetty.JettyServletTester;

public class Utils {
	@FunctionalInterface
	public interface TestBody {
		public void call(Mech2WithBase mech2) throws Exception;
	}

	public static void test(JettyServletTester.ServletCallback servlet,
			TestBody body)
			throws Exception {
		Server server = new Server(0);
		ServletContextHandler context = new ServletContextHandler(
				server,
				"/",
				ServletContextHandler.SESSIONS
				);
		ServletHolder servletHolder = new ServletHolder(new HttpServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void service(HttpServletRequest req,
					HttpServletResponse resp)
					throws ServletException, IOException {
				servlet.service(req, resp);
			}
		});
		String tmpDirName = System.getProperty("java.io.tmpdir");
		servletHolder.getRegistration().setMultipartConfig(
				new MultipartConfigElement(tmpDirName));
		context.addServlet(servletHolder, "/*");
		server.setStopAtShutdown(true);
		server.start();
		ServerConnector connector = (ServerConnector) server
				.getConnectors()[0];
		int port = connector.getLocalPort();
		URI baseURI = new URI("http://127.0.0.1:" + port);
		Mech2 mech2 = Mech2.builder().build();
		body.call(new Mech2WithBase(mech2, baseURI));
	}
}
