package com.kaihang.service;

import com.kaihang.model.User;

public interface UserService {
    // 客户端通过这个接口调用服务端的实现类
    User getUserByUserId(Integer id);

    Integer insertUser(User user);
}