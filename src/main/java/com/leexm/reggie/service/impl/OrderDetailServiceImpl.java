package com.leexm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leexm.reggie.entity.OrderDetail;
import com.leexm.reggie.mapper.OrderDetailMapper;
import com.leexm.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderDetailServiceImpl
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
