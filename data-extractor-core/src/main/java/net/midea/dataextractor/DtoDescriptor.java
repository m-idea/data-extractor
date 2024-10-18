package net.midea.dataextractor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DtoDescriptor {
    private String name;
    private String version;
    private DtoDiscriminator discriminator;
    private byte[] eol;
    private List<DtoFilter> excludes;
    private List<DtoDescriptorRecord> headers;
    private List<DtoDescriptorRecord> content;
    private List<DtoDescriptorRecord> trailers;
}
