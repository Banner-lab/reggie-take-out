package com.leexm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leexm.reggie.common.R;
import com.leexm.reggie.dto.SetmealDto;
import com.leexm.reggie.entity.Category;
import com.leexm.reggie.entity.Setmeal;
import com.leexm.reggie.entity.SetmealDish;
import com.leexm.reggie.service.CategoryService;
import com.leexm.reggie.service.SetMealService;
import com.leexm.reggie.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SetmealController
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("新增套餐信息：{}",setmealDto.toString());
        setMealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page : {},pageSize: {}",page,pageSize);
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);

        setMealService.page(setmealPage,queryWrapper);

        Page<SetmealDto> pageInfo = new Page<>(page,pageSize);

        BeanUtils.copyProperties(setmealPage, pageInfo,"records");
        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> list = new ArrayList<>();

        records.forEach(r->{
            Category category = categoryService.getById(r.getCategoryId());
            SetmealDto setmealDto = new SetmealDto();
            setmealDto.setCategoryName(category.getName());
            BeanUtils.copyProperties(r,setmealDto);
            list.add(setmealDto);
        });

        pageInfo.setRecords(list);

        return R.success(pageInfo);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache" ,allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids : {}",ids);
        setMealService.deleteWithDish(ids);
        return R.success("删除套餐成功");
    }

    @PostMapping("/status/{status}")
    public R<String> close(@PathVariable Integer status,@RequestParam List<Long> ids){
        log.info("ids : {}" ,ids);

        //update set status = ? from setmeal where id in ()
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);

        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setMealService.update(setmeal,queryWrapper);

        return R.success("修改售卖状态 成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        log.info("要修改的id是{}",id);

        Setmeal meal = setMealService.getById(id);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);

        List<SetmealDish> dishes = setmealDishService.list(setmealDishLambdaQueryWrapper);
        SetmealDto setmealDto = new SetmealDto();
        setmealDto.setSetmealDishes(dishes);

        BeanUtils.copyProperties(meal,setmealDto);

        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("修改的套餐信息: {}",setmealDto);

        setMealService.updateWithDish(setmealDto);

        return R.success("修改套餐成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,1);
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);

        List<Setmeal> list = setMealService.list(queryWrapper);

        return R.success(list);
    }
}
