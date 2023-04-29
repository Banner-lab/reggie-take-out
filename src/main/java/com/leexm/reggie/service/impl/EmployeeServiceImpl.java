package com.leexm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leexm.reggie.entity.Employee;
import com.leexm.reggie.mapper.EmployeeMapper;
import com.leexm.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @ClassName EmployeeServiceImpl
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
