package com.leexm.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leexm.reggie.common.CustomException;
import com.leexm.reggie.entity.Category;
import com.leexm.reggie.entity.Dish;
import com.leexm.reggie.entity.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
