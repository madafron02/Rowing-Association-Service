package nl.tudelft.sem.template.matching.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;

@Converter
public class TimeslotAppConverter implements AttributeConverter<TimeslotApp, String> {

    @Override
    public String convertToDatabaseColumn(TimeslotApp attribute) {
        return attribute.toString();
    }

    @Override
    public TimeslotApp convertToEntityAttribute(String dbData) {
        String[] strings = dbData.split("--");
        return new TimeslotApp(LocalDateTime.parse(strings[0]), LocalDateTime.parse(strings[1]));
    }
}
