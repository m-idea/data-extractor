package net.midea.dataextractor;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DtoFieldType {
    STRING,
    NUMBER,
    DATE,
    TIME,
    DATETIME,
    INSTANT,
    BINARY;


    @JsonCreator
    public static DtoFieldType forValue(String value) {
        return DtoFieldType.valueOf(value.toUpperCase());
    }
}