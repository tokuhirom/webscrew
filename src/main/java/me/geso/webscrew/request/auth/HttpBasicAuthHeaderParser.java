package me.geso.webscrew.request.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Authorization header parser.
 * 
 * {@link https://www.ietf.org/rfc/rfc2617.txt}
 */
public class HttpBasicAuthHeaderParser {
	// "case-insensitive token to identify the authentication scheme" in RFC2617
	private static final Pattern pattern = Pattern.compile(
			"\\ABasic (.*)\\z", Pattern.CASE_INSENSITIVE);

	/**
	 * Parse basic authorization header.
	 * 
	 * @param authHeader Get the "Authorization" header from the HTTP request.
	 * @return Parsing result. If there is not a valid header string, .
	 */
	public static Optional<HttpBasicAuthUserPass> parse(String authHeader) {
		if (authHeader == null) {
			return Optional.empty();
		}

		final Matcher matcher = pattern.matcher(authHeader);
		if (matcher.matches()) {
			String token = matcher.group(1);

			byte[] decoded;
			try {
				// Base64$Decoder throws IllegalArgumentException if there is
				// bad character pattern in base64 string.
				// It's not expected behaviour for this method.
				// This method should return `Optional.empty()`.
				decoded = Base64.getDecoder().decode(token);
			} catch (IllegalArgumentException e) {
				return Optional.empty();
			}

			String decodedString = new String(decoded,
					StandardCharsets.UTF_8);
			String[] split = decodedString.split(":", 2);
			if (split.length == 2) {
				return Optional.of(new HttpBasicAuthUserPass(split[0],
						split[1]));
			} else {
				return Optional.empty();
			}
		} else {
			return Optional.empty();
		}
	}
}
