package com.leexm.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leexm.reggie.common.CustomException;
import com.leexm.reggie.entity.Category;
import com.leexm.reggie.entity.Dish;
import com.leexm.reggie.entity.Setmeal;
import com.leexm.reggie.mapper.CategoryMapper;
import com.leexm.reggie.service.CategoryService;
import com.leexm.reggie.service.DishService;
import com.leexm.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CategoryServiceImpl
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id );
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count > 0){
            throw new CustomException("无法删除，该分类下存在菜品");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setMealService.count(setmealLambdaQueryWrapper);
        if(count1 > 0){
            throw new CustomException("无法删除，该分类下存在套餐");
        }

        super.removeById(id);
    }
}
