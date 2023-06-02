package com.wang.order.service;

import com.wang.order.mapper.OrderMapper;
import com.wang.order.pojo.Order;
import com.wang.order.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;
    public Order queryOrderById(Long orderId){
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.利用RestTemplate 发起http请求，查询用户
        //// 2.1 获取url路径
        //String url="http://localhost:8081/user/"+order.getUserId();

        // 修改访问的url路径，用服务名代替ip、端口：  application: name: userservice
        String url="http://userservice/user/"+order.getUserId();
        // 2.2 发送http请求，实现远程调用
        User user = restTemplate.getForObject(url, User.class);
        //3.存入order
        order.setUser(user);
        // 4.返回
        return order;
    }

}
