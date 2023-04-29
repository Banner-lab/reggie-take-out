package com.leexm.reggie.dto;


import com.leexm.reggie.entity.Setmeal;
import com.leexm.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
