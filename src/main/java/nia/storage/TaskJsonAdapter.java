package nia.storage;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import nia.tasks.Deadline;
import nia.tasks.Event;
import nia.tasks.Task;
import nia.tasks.Todo;

/**
 * Teaches Gson how to (de)serialize Task, an abstract class - Gson's default
 * reflection-based handling can't know which concrete subclass (Todo, Deadline,
 * or Event) a bare Task field should become. Adds a "type" field to the JSON
 * (independent of Task#getTag()'s single-letter "T"/"D"/"E", which is display-only)
 * that records which subclass produced the object, and dispatches on it when
 * reading one back.
 */
public class TaskJsonAdapter implements JsonSerializer<Task>, JsonDeserializer<Task> {
    private static final String TYPE_TODO = "todo";
    private static final String TYPE_DEADLINE = "deadline";
    private static final String TYPE_EVENT = "event";

    /** Serializes src to a JSON object tagged with its concrete type ("todo"/"deadline"/"event"). */
    @Override
    public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("description", src.getDescription());
        json.addProperty("isDone", src.isDone());

        if (src instanceof Deadline deadline) {
            json.addProperty("type", TYPE_DEADLINE);
            json.add("by", context.serialize(deadline.getBy(), LocalDateTime.class));
        } else if (src instanceof Event event) {
            json.addProperty("type", TYPE_EVENT);
            json.add("from", context.serialize(event.getFrom(), LocalDateTime.class));
            json.add("to", context.serialize(event.getTo(), LocalDateTime.class));
        } else {
            json.addProperty("type", TYPE_TODO);
        }
        return json;
    }

    /** Deserializes json back into the concrete Task subclass its "type" field names. */
    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        String description = obj.get("description").getAsString();

        Task task = switch (type) {
            case TYPE_TODO -> new Todo(description);
            case TYPE_DEADLINE -> new Deadline(description, deserializeDateTime(obj, "by", context));
            case TYPE_EVENT -> new Event(
                    description,
                    deserializeDateTime(obj, "from", context),
                    deserializeDateTime(obj, "to", context));
            default -> throw new JsonParseException("unknown task type '" + type + "'");
        };

        if (obj.get("isDone").getAsBoolean()) {
            task.markAsDone();
        }
        return task;
    }

    private LocalDateTime deserializeDateTime(JsonObject obj, String field, JsonDeserializationContext context) {
        return context.deserialize(obj.get(field), LocalDateTime.class);
    }
}
