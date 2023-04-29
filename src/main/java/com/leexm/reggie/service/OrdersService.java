package com.leexm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leexm.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
