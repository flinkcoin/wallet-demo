package org.flinkcoin.wallet.helper;

/*-
 * #%L
 * Wallet - Helper
 * %%
 * Copyright (C) 2021 Flink Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSerializers {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializers.class);

    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public LocalDate deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
            try {
                return LocalDateTime.parse(jp.getText(), DATE_TIME_FORMATTER).toLocalDate();
            } catch (IllegalArgumentException e) {
                LOGGER.error("Excpetion", e);
                return null;
            }
        }
    }

    public static class LocalDateSerializer extends JsonSerializer<LocalDate> {

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public void serialize(LocalDate t, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeString(DATE_TIME_FORMATTER.format(t));
        }
    }

    public static class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

        private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public LocalTime deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
            try {
                return LocalDateTime.parse(jp.getText(), dtf).toLocalTime();
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static class LocalTimeSerializer extends JsonSerializer<LocalTime> {

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public void serialize(LocalTime t, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeString(DATE_TIME_FORMATTER.format(t));
        }
    }
}
