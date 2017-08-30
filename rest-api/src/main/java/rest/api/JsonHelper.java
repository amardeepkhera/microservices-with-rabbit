package rest.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonHelper {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public <T> T fromJson(final String json, final Class<T> to) {
		try {
			return OBJECT_MAPPER.readValue(json, to);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T fromJsonAsByteArray(final byte[] json, final Class<T> to) {
		try {
			return OBJECT_MAPPER.readValue(json, to);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String toJson(final Object from) {
		try {
			return OBJECT_MAPPER.writeValueAsString(from);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] toJsonAsByteArray(final Object from) {
		try {
			return OBJECT_MAPPER.writeValueAsBytes(from);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
