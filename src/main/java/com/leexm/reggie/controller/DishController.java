package com.leexm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leexm.reggie.common.CustomException;
import com.leexm.reggie.common.R;
import com.leexm.reggie.dto.DishDto;
import com.leexm.reggie.entity.Category;
import com.leexm.reggie.entity.Dish;
import com.leexm.reggie.entity.DishFlavor;
import com.leexm.reggie.entity.SetmealDish;
import com.leexm.reggie.mapper.DishMapper;
import com.leexm.reggie.service.CategoryService;
import com.leexm.reggie.service.DishFlavorService;
import com.leexm.reggie.service.DishService;
import com.leexm.reggie.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DishController
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R <String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page:{} , pageSize: {}, name: {}",page,pageSize,name);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByAsc(Dish::getSort);

        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();
        records.forEach(r->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(r,dishDto);

            Long categoryId = r.getCategoryId();
            Category category = categoryService.getById(categoryId);//根据id查询分类对象
            String catename = category.getName();
            dishDto.setCategoryName(catename);
            list.add(dishDto);
        });

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> query(@PathVariable Long id){
        log.info("要寻找的id:{}",id);

        DishDto dishDto = dishService.getByIdWithFalvor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品信息
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("修改菜品成功");
    }


    //@GetMapping("/list")
    //public R<List> list(Dish dish){
    //    log.info("categoryId: {}",dish.toString());

    //    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());

    //    queryWrapper.eq(Dish::getStatus,1);

    //    queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);

    //    List<Dish> list = dishService.list(queryWrapper);
    //    return R.success(list);
    //}

    @GetMapping("/list")
    public R<List> list(Dish dish){
        List<DishDto> dishDtos = null;

        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishDtos = (List<DishDto>)redisTemplate.opsForValue().get(key);

        if(dishDtos != null){
            return R.success(dishDtos);
        }



        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);


        List<DishDto> finalDishDtos = new ArrayList<>();
        list.forEach(l->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(l,dishDto);


            Category category = categoryService.getById(l.getCategoryId());
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);

            Long dishId = l.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);

            List<DishFlavor> falvors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(falvors);
            finalDishDtos.add(dishDto);
        });

        redisTemplate.opsForValue().set(key,finalDishDtos,60, TimeUnit.MINUTES);

        return R.success(finalDishDtos);
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,@RequestParam List<Long> ids){
        log.info("ids : {}" ,ids);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);


        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId,ids);
        int count = setmealDishService.count(setmealDishLambdaQueryWrapper);
        if(count > 0 && status == 0) {
            throw new CustomException("无法停售状态，菜品在套餐中售卖");
        }

        Dish dish = new Dish();
        dish.setStatus(status);

        dishService.update(dish,queryWrapper);
        return R.success("修改售卖状态成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("要删除的id是: {}",ids);
        dishService.deleteDish(ids);
        return R.success("删除成功");
    }

}
