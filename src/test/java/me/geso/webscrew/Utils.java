package me.geso.webscrew;

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
		JettyServletTester.runServlet(servlet, (uri) -> {
			Mech2 mech2 = Mech2.builder().build();
			body.call(new Mech2WithBase(mech2, uri));
		});
	}
}
