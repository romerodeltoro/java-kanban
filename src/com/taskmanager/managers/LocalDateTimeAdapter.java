package com.taskmanager.managers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDate) throws IOException {
        //     jsonWriter.value(localDate.format(formatterWriter));

        if (localDate != null) {
            jsonWriter.value(localDate.format(formatter));
        } else {
            jsonWriter.value((String) null);
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatter);
    }
}
