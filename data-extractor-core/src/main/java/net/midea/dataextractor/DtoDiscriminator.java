package net.midea.dataextractor;

import lombok.Data;

@Data
public class DtoDiscriminator {
    int start;
    int end;
    DtoFieldType type;
}