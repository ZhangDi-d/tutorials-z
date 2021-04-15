内容基本来自 https://www.iocoder.cn/Spring-Boot/Netty/?github，学习使用

包含：  
1.netty的简单示例

2. @Restful 风格的@PathVariable 性能优化测试








请求：

```shell

###
POST http://localhost:8082/test1?type=AUTH_REQUEST&message={\"accessToken\":\"zhangsan\"}
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8

###
POST http://localhost:8082/test2
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8

{
"type": "AUTH_REQUEST",
"message": "{\"accessToken\":\"zhangsan\"}"
}


###
POST http://localhost:8083/test2
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8

{
"type": "AUTH_REQUEST",
"message": "{\"accessToken\":\"lisi\"}"
}

###
POST http://localhost:8083/test2
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8

{
"type": "CHAT_SEND_TO_ONE_REQUEST",
"message": "{\"toUser\":\"zhangsan\",\"msgId\":\"1\",\"content\":\"haha\"}"
}


###
POST http://localhost:8083/test2
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8

{
"type": "CHAT_SEND_TO_ALL_REQUEST",
"message": "{\"msgId\":\"1\",\"content\":\"haha\"}"
}
```