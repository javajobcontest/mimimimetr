package com.inovus.mimimimetr.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatDto {
    private Long id;
    private String name;
    private String pictureUrl;

    public static CatDto fromCat(Cat cat) {
        CatDto catDto = new CatDto();
        catDto.id = cat.getId();
        catDto.name = cat.getName();
        catDto.pictureUrl = cat.getPictureUrl();
        return catDto;
    }
}
