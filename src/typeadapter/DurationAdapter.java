package typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

import java.time.temporal.ChronoUnit;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        jsonWriter.value(duration.get(ChronoUnit.SECONDS));
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Duration.ofSeconds(Long.parseLong(jsonReader.nextString()));
    }
}