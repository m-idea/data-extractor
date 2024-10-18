package net.midea.dataextractor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DtoDescriptorField {
    private String name;
    @JsonProperty("const")
    private String constant;
    private boolean discriminator;
    private String correlationKey;
    private DtoFieldType type;
    private int start;
    private int end;
    private boolean optional;
    private String format;

    public static String getDateFormat(String format) {
        return format.replace("Y", "y").replace("D", "d").replace("h", "H");
    }

    public String getDateFormat() {
        if (format != null)
            return DtoDescriptorField.getDateFormat(format);
        throw new RuntimeException("DateFormat not found");
    }
}