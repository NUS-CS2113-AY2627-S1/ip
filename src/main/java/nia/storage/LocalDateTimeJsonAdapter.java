package nia.storage;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Teaches Gson how to (de)serialize LocalDateTime, a type it has no built-in
 * adapter for. Stored as a plain ISO-8601 string (LocalDateTime's own
 * toString()/parse() format, e.g. "2019-10-15T18:00:00") - the standard,
 * idiomatic representation for JSON, and it needs no custom pattern.
 */
public class LocalDateTimeJsonAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    /** Serializes src to its ISO-8601 string representation. */
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    /** Deserializes json's ISO-8601 string back into a LocalDateTime. */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString());
    }
}
