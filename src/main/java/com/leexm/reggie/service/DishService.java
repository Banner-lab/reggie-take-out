package com.leexm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leexm.reggie.dto.DishDto;
import com.leexm.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //添加菜品的同时添加口味数据
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFalvor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteDish(List<Long> ids);
}
