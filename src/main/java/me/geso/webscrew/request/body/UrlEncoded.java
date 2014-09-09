package me.geso.webscrew.request.body;

import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.MultiValueMap;

import lombok.SneakyThrows;
import me.geso.webscrew.Parameters;

public class UrlEncoded {
	@SneakyThrows
	public static Parameters parseQueryString(String queryString,
			String encoding) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		MultiValueMap<String, String> query =
				MapUtils.multiValueMap(new LinkedHashMap(), LinkedHashSet.class);
		if (queryString != null) {
			for (String pair : queryString.split("&")) {
				int eq = pair.indexOf("=");
				if (eq < 0) {
					// key with no value
					query.put(URLDecoder.decode(pair, encoding), "");
				} else {
					// key=value
					String key = URLDecoder.decode(pair.substring(0, eq),
							encoding);
					String value = URLDecoder.decode(pair.substring(eq + 1),
							encoding);
					query.put(key, value);
				}
			}
		}
		return new Parameters(query);
	}

}
