package com.leexm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leexm.reggie.dto.DishDto;
import com.leexm.reggie.dto.SetmealDto;
import com.leexm.reggie.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);
    void deleteWithDish(List<Long> ids);

    void updateWithDish(SetmealDto setmealDto);
}
