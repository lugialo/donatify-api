package com.lugialo.donatify.dto;

import com.lugialo.donatify.model.Ong;
import lombok.Data;

@Data
public class OngDto {
    private Long id;
    private String name;


    public static OngDto fromEntity(Ong ong) {
        OngDto dto = new OngDto();
        dto.setId(ong.getId());
        dto.setName(ong.getName());
        return dto;
    }
}