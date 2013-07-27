package ru.nsk.test.cabinet.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Simply JSON date serializer to "yyyy-MM-dd" format.
 * @author me
 */
@Slf4j
@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        log.trace("Raw date: {}" + date);
        String formattedDate = dateFormat.format(date);

        log.trace("Serialized date: {}" + formattedDate);
        gen.writeString(formattedDate);
    }
}
