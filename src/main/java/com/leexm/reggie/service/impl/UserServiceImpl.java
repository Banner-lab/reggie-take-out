package com.leexm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leexm.reggie.entity.User;
import com.leexm.reggie.mapper.UserMappper;
import com.leexm.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMappper, User> implements UserService {
}
