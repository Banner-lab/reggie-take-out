package com.leexm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leexm.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMappper extends BaseMapper<User> {
}
