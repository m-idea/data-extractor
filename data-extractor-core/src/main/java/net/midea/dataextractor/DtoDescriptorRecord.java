package net.midea.dataextractor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DtoDescriptorRecord {
    transient private Map<String, DtoDescriptorField> map;
    private String name;
    private List<DtoDescriptorField> fields;
    private RecordType type;

    public synchronized Map<String, DtoDescriptorField> getMap() {
        if (map == null) {
            map = new HashMap<>();

            for (DtoDescriptorField f : fields) {
                map.put(f.getName(), f);
            }
        }
        return map;
    }
}