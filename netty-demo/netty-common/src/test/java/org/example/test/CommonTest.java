package org.example.test;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.AbstractTest;
import org.example.entity.MyTestBean;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author dizhang
 * @date 2021-04-08
 */

public class CommonTest extends AbstractTest {

    Logger logger = LogManager.getLogger(CommonTest.class);

    public static String getMd5(String str) {
        String result = "";
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes(StandardCharsets.UTF_8));
            byte[] b = md5.digest();
            int i;
            StringBuilder builder = new StringBuilder("");
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    builder.append("0");
                }
                builder.append(Integer.toHexString(i));
            }
            result = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    @BeforeEach
    void testbefore() {
        logger.info("before...");
    }

    @Test
    void test1() {
        logger.info("test1...");
        final byte[] bytes = new byte[1024];
        long l = System.currentTimeMillis();
        ByteBuf byteBuf;
        for (int i = 0; i < 100000; i++) {
            byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            byteBuf.writeBytes(bytes);
            byteBuf.release();
        }
        logger.info("pool time: {} ms", System.currentTimeMillis() - l);
    }

    @Test
    void test2() {
        logger.info("test2...");
        final byte[] bytes = new byte[1024];
        long l = System.currentTimeMillis();
        ByteBuf byteBuf;
        for (int i = 0; i < 100000; i++) {
            byteBuf = Unpooled.directBuffer(1024);
            byteBuf.writeBytes(bytes);
            byteBuf.release();
        }
        logger.info("un pool time: {} ms", System.currentTimeMillis() - l);
    }

    @Test
    void testServer() {

        //eventLoop-1-XXX

        //Netty封装了NIO，Reactor模型，Boss，worker
        // Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            //1、创建对象
            // Netty服务
            //ServetBootstrap   ServerSocketChannel
            ServerBootstrap server = new ServerBootstrap();


            //2、配置参数
            // 链路式编程
            server.group(bossGroup, workerGroup)
                    // 主线程处理类,看到这样的写法，底层就是用反射
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类 , Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 客户端初始化处理
                        protected void initChannel(SocketChannel client) throws Exception {
                            // 无锁化串行编程
                            //Netty对HTTP协议的封装，顺序有要求
                            // HttpResponseEncoder 编码器
                            // 责任链模式，双向链表Inbound OutBound
                            client.pipeline().addLast(new HttpResponseEncoder());
                            // HttpRequestDecoder 解码器
                            client.pipeline().addLast(new HttpRequestDecoder());

                        }
                    })
                    // 针对主线程的配置 分配线程最大数量 128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 针对子线程的配置 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //3、启动服务器
            ChannelFuture f = server.bind(8083).sync();
            f.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info("close..");
                }
            });
//            System.out.println("已启动，监听的端口是：" + 8083);
//            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Test
    void testClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            //1、创建对象
            Bootstrap client = new Bootstrap();

            //2、配置参数
            // 链路式编程
            client.group(group)
                    // 主线程处理类,看到这样的写法，底层就是用反射
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 子线程处理类 , Handler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        // 客户端初始化处理
                        protected void initChannel(SocketChannel client) throws Exception {
                            logger.info("client initChannel...");
                        }
                    });

            //3 连接服务器
            ChannelFuture channelFuture = client.connect("127.0.0.1", 8083).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    @Test
    void test5() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        List<Integer> integers = list1.subList(0, 2);
        System.out.println(integers);
        System.out.println(list1);
        integers = list1.subList(0, 4);
        System.out.println(integers);

        List<Integer> list2 = new ArrayList<>();

        list2.addAll(integers);

        System.out.println(list2);
//        list2.add(2);
//        list2.add(4);
//        list2.add(10);
//        boolean b = list2.removeAll(list1);
//
//        System.out.println(list2);
    }

    @Test
    void test10() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        String md5 = getMd5(list1.toString());
        System.out.println(md5);  //6675982b5d76c7fdd870c9e439c0b413
    }

    @Test
    void test11() {
        String a = "vova-rec:recent-visited-ranking-list-00001-1618941963";
        String b = "vova-rec:recent-visited-ranking-list-00001-1618884750";
        String c = "vova-rec:recent-visited-ranking-list-00001-1618971160";

        HashSet<String> objects = new HashSet<>();
        objects.add(a);
        objects.add(b);
        objects.add(c);

        ArrayList<String> strings = new ArrayList<>(objects);
        System.out.println(strings);
        Collections.sort(strings);

        System.out.println(strings);

    }

    @Test
    public void test13() {
        final long currentSec = LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z"));
        System.out.println(currentSec);
        final long currentSec1 = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(currentSec1);

        System.out.println(new Date());
    }

    @Test
    public void test14() {
        final long currentSec = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        long expiredPoint = currentSec - 86400;
        System.out.println(expiredPoint);  //1618890608
        String key = String.format("vova-rec:recent-visited-ranking-list-%s-%s", "*", "*");
        System.out.println(key);

        char[] chars = String.valueOf(expiredPoint).toCharArray();
        for (char aChar : chars) {
            System.out.print(aChar + " ");

        }
        System.out.println();
        char c3 = chars[3];
        char c4 = chars[4];
        char c5 = chars[5];
        char c6 = chars[6];
        char c7 = chars[7];
        char c8 = chars[8];
        char c9 = chars[9];

        System.out.println(c3 + " " + c4 + " " + c5 + " " + c6 + " " + c7 + " " + c8 + " " + c9);


        // 1618890608  = 161889060[0-chars[9]]||16188906[0-chars[8]][0-chars[9]]||16188906[0-chars[8]]*


    }

    @Test
    public void test15() {
        MultiValueMap<String, String> stringMultiValueMap = new LinkedMultiValueMap<>();

        stringMultiValueMap.add("湖人", "科比");
        stringMultiValueMap.add("湖人", "詹姆斯");
        stringMultiValueMap.add("勇士", "库里");
        stringMultiValueMap.add("乡村爱情", "赵四");
        stringMultiValueMap.add("乡村爱情", "刘能");


        Set<String> keySet = stringMultiValueMap.keySet();
        for (String key : keySet) {
            List<String> values = stringMultiValueMap.get(key);
            assert values != null;
            System.out.println(key + " " + StringUtils.join(values.toArray(), " "));

        }
    }

    @Test
    public void test16() {

        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("a", "b");
        aa aa = new aa();
        aa.setName("1");
        aa.setObjectObjectHashMap(objectObjectHashMap);
        String s = JSONObject.toJSONString(aa);
        System.out.println(s);
    }

    @Data
    static class aa {
        private HashMap<String, String> objectObjectHashMap;
        private String name;
    }

    @Test
    public void test17() {
        List<User> users = new ArrayList<>();
        users.add(new User("zhagnsan",Arrays.asList("爬山","游泳")));
        users.add(new User("lisi",Arrays.asList("足球","篮球")));
        users.add(new User("lisi",Arrays.asList("足球","篮球","乒乓球")));
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getName, Function.identity(), (key1, key2) -> key2));
        System.out.println(userMap);


    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSimpleLoad() {
        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));
        MyTestBean bean = (MyTestBean) bf.getBean("myTestBean");
        assertEquals("testStr", bean.getTestStr());
    }

    public static String hex2Binary64bitAndRecallFlag(String hexString) {
        StringBuilder preBin = new StringBuilder(new BigInteger(hexString, 16).toString(2));
        int length = preBin.length();
        if (length < 64) {
            for (int i = 0; i < 64 - length; i++) {
                preBin.insert(0, "0");
            }
        }
        String binString = preBin.toString();

        StringBuilder index = new StringBuilder();
        for (int i = 0; i < binString.length(); i++) {
            if ("1".equals(String.valueOf(binString.charAt(i)))) {
                index.append(i).append(" ");
            }
        }
        return index.toString();
    }
}



