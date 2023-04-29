package com.leexm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leexm.reggie.entity.AddressBook;
import com.leexm.reggie.mapper.AddressBookMapper;
import com.leexm.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @ClassName AddressBookImpl
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Service
public class AddressBookImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
