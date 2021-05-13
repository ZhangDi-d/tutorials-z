package org.example.test;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    /**
     * 16 -> 2 不足补0  并且输出  recall flag index
     *
     * @param hexString
     * @return
     */
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

    @BeforeEach
    void testbefore() {
        logger.info("before...");
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
        users.add(new User("zhagnsan", Arrays.asList("爬山", "游泳")));
        users.add(new User("lisi", Arrays.asList("足球", "篮球")));
        users.add(new User("lisi", Arrays.asList("足球", "篮球", "乒乓球")));
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


    @Test
    public void test18() {
        Properties properties = new Properties();
        properties.put("zhangsan", "111");
        properties.put("lisi", "111");

        HashMap<String, String> map2 = new HashMap<>();
        map2.put("wangwu", "222");
        CollectionUtils.mergePropertiesIntoMap(properties, map2);

        System.out.println(map2);
    }


    @Test
    public void test20() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = mapper.readValue(new File("D:\\Work\\Learn\\alibaba\\nacos\\nacos-group\\nacos\\test\\src\\test\\resources\\aa.json"),
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                    });
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap) map.get("data");
            ArrayList<Integer> goods_id_list = (ArrayList) linkedHashMap.get("goods_id_list");
            LinkedHashMap<String, LinkedHashMap> goods_search_info_list = (LinkedHashMap) linkedHashMap.get("goods_search_info_list");

            for (Integer integer : goods_id_list) {
                LinkedHashMap<String, String> linkedHashMap1 = goods_search_info_list.get(String.valueOf(integer));
                String recall_pool = linkedHashMap1.get("recall_pool");
                System.out.println(integer + " :[ " + hex2Binary64bitAndRecallFlag(recall_pool) + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void test21() {
        System.out.println("8000000000000000 " + hex2Binary64bitAndRecallFlag("8000000000000000"));  // 0
        System.out.println("0200000000000808 " + hex2Binary64bitAndRecallFlag("0200000000000808"));  //6 52 60
        System.out.println("0200000000000800 " + hex2Binary64bitAndRecallFlag("0200000000000800"));  //6 52
        System.out.println("0000000000000808 " + hex2Binary64bitAndRecallFlag("0000000000000808"));  //52 60
        System.out.println("0200000000000000 " + hex2Binary64bitAndRecallFlag("0200000000000000"));  //6
        System.out.println("0200000000000008 " + hex2Binary64bitAndRecallFlag("0200000000000008"));  //6 60


        System.out.println("0000000000000001 " + hex2Binary64bitAndRecallFlag("0000000000000001"));  //61
        System.out.println("0000000000000002 " + hex2Binary64bitAndRecallFlag("0000000000000002"));  //62
        System.out.println("0000000000000004 " + hex2Binary64bitAndRecallFlag("0000000000000004"));  //63

        System.out.println("0000000000200000 " + hex2Binary64bitAndRecallFlag("0000000000200000"));  // 42
        System.out.println("1200000000000808 " + hex2Binary64bitAndRecallFlag("1200000000000808"));  // 3 6 52 60
        System.out.println("1000000000000000 " + hex2Binary64bitAndRecallFlag("1000000000000000"));  // 3

        System.out.println("0000001000000000 " + hex2Binary64bitAndRecallFlag("0000001000000000"));  // 27
        System.out.println("0000001000010000 " + hex2Binary64bitAndRecallFlag("0000001000010000"));  // 27 47


        System.out.println("0000000000210000 " + hex2Binary64bitAndRecallFlag("0000000000210000"));  // 42 47
        System.out.println("1000000000010000 " + hex2Binary64bitAndRecallFlag("1000000000010000"));  // 3 47
        System.out.println("0000000800010000 " + hex2Binary64bitAndRecallFlag("0000000800010000"));  // 28 47


    }


    @Test
    void test22() {
        String json = """
                {
                      "goods_search_info_list" : {
                               "12394772" : {
                                  "recall_pool" : "0000001000000000"
                               },
                               "17816544" : {
                                  "recall_pool" : "0000000000000002"
                               },
                               "25029722" : {
                                  "recall_pool" : "0000000000000008"
                               },
                               "37037316" : {
                                  "recall_pool" : "0001000000000000"
                               },
                               "46828413" : {
                                  "recall_pool" : "8000000000000000"
                               },
                               "47274307" : {
                                  "recall_pool" : "0000000000000002"
                               },
                               "50126464" : {
                                  "recall_pool" : "0040000000000000"
                               },
                               "65146974" : {
                                  "recall_pool" : "8000000000000000"
                               },
                               "73932889" : {
                                  "recall_pool" : "8000000000000000"
                               },
                               "74876813" : {
                                  "recall_pool" : "0000000000000008"
                               }
                            }
                      
                }              
                """;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map;

        try {
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap) map.get("goods_search_info_list");
            for (Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
                LinkedHashMap<String, Object> inner = (LinkedHashMap) linkedHashMap.get(entry.getKey());
                String recallPool = (String) inner.get("recall_pool");
                System.out.println(entry.getKey() + " :[ " + hex2Binary64bitAndRecallFlag(recallPool) + "]");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    void test11111() {
        List<String> reqResList = new ArrayList<>();
        reqResList.add("current date=" + (new Date()));
        System.out.println(reqResList);
        System.out.println(reqResList);
    }

    @Test
    void test221() {
        List<User> users = Arrays.asList(new User("zhagnsan", new ArrayList<>()), new User("lisi", new ArrayList<>()), new User("wangwu", new ArrayList<>()));
        List<User> collect = users.stream().peek(user -> {
            if (user.getName().equals("lisi")) {
                user.setHabits(Collections.singletonList("pashang"));
            }
        }).collect(Collectors.toList());

        System.out.println(collect);
    }

    @Test
    void test222() {
        ArrayList<Integer> objects0 = new ArrayList<>();
        objects0.add(0);
        objects0.add(1);
        objects0.add(2);

    }
}






