package io.ticketing.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.random.RandomGenerator;

@Slf4j
@Component
public class SharedUtils {
    private final RandomGenerator randomGenerator;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public SharedUtils(RandomGenerator randomGenerator, ObjectMapper objectMapper, Validator validator) {
        this.randomGenerator = randomGenerator;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public boolean isNullOrEmptyOrBlank(String s) {
        return s == null || s.isEmpty() || s.isBlank();
    }

    public String toJson(Object o, boolean prettify) {
        if (o != null) {
            try {
                return prettify ? this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o) : this.objectMapper.writeValueAsString(o);
            } catch (Exception e) {
                log.error(String.format("Object to json error [ %s ]", o), e);
            }
        }
        return null;
    }

    @SneakyThrows
    public <T> T fromJsonToObject(String json, Class<T> c) {
        return this.objectMapper.readValue(json, c);
    }

    @SneakyThrows
    public <T> T fromJsonToObject(String json, TypeReference<T> c) {
        return this.objectMapper.readValue(json, c);
    }

    @SneakyThrows
    public String formatDate(@NonNull String format, @NonNull LocalDateTime time) {
        if (!this.isNullOrEmptyOrBlank(format)) {
            return DateTimeFormatter.ofPattern(format).format(time);
        } else {
            throw new Exception(String.format("Invalid date format %s for date %s", format, time));
        }
    }

    @SneakyThrows
    public LocalDateTime toDate(@NonNull String format, @NonNull String time, @NonNull String zone) {
        return new SimpleDateFormat(format).parse(time).toInstant().atZone(ZoneId.of(zone)).toLocalDateTime();
    }

    public int calculateAge(LocalDateTime dateOfBirth) {
        LocalDateTime currentDate = LocalDateTime.now();
        return Period.between(dateOfBirth.toLocalDate(), currentDate.toLocalDate()).getYears();
    }

    private String randomAlphaNumeric(int size) {
        return this.randomGenerator.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String randomNumeric(int size) {
        return this.randomGenerator.ints(48, 58)
                .filter(i -> i <= 57)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public boolean isNumeric(String s) {
        try {
            if (!this.isNullOrEmptyOrBlank(s)) {
                double d = Double.parseDouble(s);
                log.debug(String.format("Numeric validation [ %s ]", d));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
