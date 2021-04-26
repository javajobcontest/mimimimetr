package com.inovus.mimimimetr.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatDao {
    public Long id;
    public String name;
    public String pictureUrl;

    public static CatDao fromCat(Cat cat) {
        CatDao catDao = new CatDao();
        catDao.id = cat.getId();
        catDao.name = cat.getName();
        catDao.pictureUrl = cat.getPictureUrl();
        return catDao;
    }
}
