package com.leexm.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leexm.reggie.common.CustomException;
import com.leexm.reggie.dto.DishDto;
import com.leexm.reggie.dto.SetmealDto;
import com.leexm.reggie.entity.Setmeal;
import com.leexm.reggie.entity.SetmealDish;
import com.leexm.reggie.mapper.SetMealMapper;
import com.leexm.reggie.service.SetMealService;
import com.leexm.reggie.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName SetmealServiceImpl
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);
        //保存setmeal和dish的关联
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.forEach(s->{
            s.setSetmealId(setmealDto.getId());
        });

        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //查询套餐状态，看能否删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            throw new CustomException("无法删除，套餐正在售卖中");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(s->{
            s.setSetmealId(setmealDto.getId());
        });

        setmealDishService.saveBatch(setmealDishes);
    }
}
