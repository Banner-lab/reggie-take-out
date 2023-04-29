package com.leexm.reggie.controller;

import com.leexm.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OrderDetailController
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
}
