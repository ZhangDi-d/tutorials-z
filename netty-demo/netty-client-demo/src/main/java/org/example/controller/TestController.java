package org.example.controller;


import org.example.client.NettyClient;
import org.example.codec.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private NettyClient nettyClient;

    @PostMapping("/test1")
    public String mock(@RequestParam("type") String type, @RequestParam("message")String message) {
        // 创建 Invocation 对象
        Invocation invocation = new Invocation(type, message);
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }


    @PostMapping("/test2")
    public String mock(@RequestBody Invocation invocation) {
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }


}
