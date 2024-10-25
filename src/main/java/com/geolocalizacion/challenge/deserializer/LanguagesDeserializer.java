package com.geolocalizacion.challenge.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LanguagesDeserializer extends JsonDeserializer<String[]> {

    @Override
    public String[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String languages = p.getText();
        if (languages == null || languages.isEmpty()) {
            return new String[0];
        }
        return languages.split(",");
    }
}