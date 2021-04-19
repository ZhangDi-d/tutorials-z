package org.example.controller;

import org.example.controller.response.Response;
import org.example.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dizhang
 * @date 2021-04-02
 */
@RestController
@RequestMapping("/rest")
public class TestRestController implements TestInter {

//    private static final List<String> strings = List.of("1");
//
//    @GetMapping(value = "/test0/{name}", headers = {"service-name=test0"})
//    public Response<String> test0(@PathVariable String name) {
//        System.out.println("test0 " + name);
//        return Response.ok(name);
//    }
//
//    @GetMapping(value = "/test1/11/11/{name}")
//    public Response<String> test111(@PathVariable String name) {
//        System.out.println("test1 " + name);
//        return Response.ok(name);
//    }
//
//    @GetMapping(value = "/test1/11/22/{name}")
//    public Response<String> test122(@PathVariable String name) {
//        System.out.println("test1 " + name);
//        return Response.ok(name);
//    }
//
//    @GetMapping(value = "/test2")
//    public Response<String> test2(@RequestParam String name) {
//        System.out.println("test2 " + name);
//        return Response.ok(name);
//    }
//
//    //=====================================================================//
//    @PostMapping(value = "test1")
//    public String norestful1(@RequestBody User user) {
//        System.out.println("user " + user.toString());
//        List<String> habits = user.getHabits();
//        if (habits == null) {
//            user.setHabits(strings);
//        } else {
//            if (!habits.contains("1")) habits.add("1");
//            user.setHabits(habits);
//        }
//        addUser(user);
//
//        return user.toString();
//    }
//
//
//    //=====================================================================//
//
//    @GetMapping(value = "restful1/{name}")
//    public String restful1(@PathVariable String name) {
//        System.out.println("restful1 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful2/{name}")
//    public String restful2(@PathVariable String name) {
//        System.out.println("restful2 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful3/{name}")
//    public String restful3(@PathVariable String name) {
//        System.out.println("restful3 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful4/{name}")
//    public String restful4(@PathVariable String name) {
//        System.out.println("restful4 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful5/{name}")
//    public String restful5(@PathVariable String name) {
//        System.out.println("restful5 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful6/{name}")
//    public String restful6(@PathVariable String name) {
//        System.out.println("restful6 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful7/{name}")
//    public String restful7(@PathVariable String name) {
//        System.out.println("restful7 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful8/{name}")
//    public String restful8(@PathVariable String name) {
//        System.out.println("restful8 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful9/{name}")
//    public String restful9(@PathVariable String name) {
//        System.out.println("restful9 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful10/{name}")
//    public String restful10(@PathVariable String name) {
//        System.out.println("restful10 " + name);
//        return name;
//    }
//
//    @GetMapping(value = "restful11/{name}")
//    public String restful11(@PathVariable String name) {
//        System.out.println("restful11 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful12/{name}")
//    public String restful12(@PathVariable String name) {
//        System.out.println("restful12 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful13/{name}")
//    public String restful13(@PathVariable String name) {
//        System.out.println("restful13 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful14/{name}")
//    public String restful14(@PathVariable String name) {
//        System.out.println("restful14 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful15/{name}")
//    public String restful15(@PathVariable String name) {
//        System.out.println("restful15 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful16/{name}")
//    public String restful16(@PathVariable String name) {
//        System.out.println("restful16 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful17/{name}")
//    public String restful17(@PathVariable String name) {
//        System.out.println("restful17 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful18/{name}")
//    public String restful18(@PathVariable String name) {
//        System.out.println("restful18 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful19/{name}")
//    public String restful19(@PathVariable String name) {
//        System.out.println("restful19 " + name);
//        return name;
//    }
//    @GetMapping(value = "restful20/{name}")
//    public String restful20(@PathVariable String name) {
//        System.out.println("restful20 " + name);
//        return name;
//    }
//
//    @GetMapping(value = "restful/{dept}/21/{name}")
//    public String restful21(@PathVariable String name) {
//        System.out.println("restful " + name);
//        return name;
//    }
//    @GetMapping(value = "restful/{dept}/22/{name}")
//    public String restful22(@PathVariable String name) {
//        System.out.println("restful " + name);
//        return name;
//    }
//    @GetMapping(value = "restful/{dept}/23/{name}")
//    public String restful23(@PathVariable String name) {
//        System.out.println("restful " + name);
//        return name;
//    }
//    @GetMapping(value = "restful/{dept}/24/{name}")
//    public String restful24(@PathVariable String name) {
//        System.out.println("restful " + name);
//        return name;
//    }
//    @GetMapping(value = "restful/{dept}/25/{name}")
//    public String restful25(@PathVariable String name) {
//        System.out.println("restful " + name);
//        return name;
//    }
//


    //----------------------------------------------------------------------------------//
    @RequestMapping(value = "/restful/{name}",headers = {"service_name=restful"},method =RequestMethod.GET ) //,headers = {"service_name=restful"}
    public String restful(@PathVariable String name) {
        System.out.println("r " + name);
        return name;
    }


    @RequestMapping(value = "/non-restful",method =RequestMethod.GET )
    public String non_restful(@RequestParam String name) {
        System.out.println("n " + name);
        return name;
    }


    @RequestMapping(value = "/restful9/{name}",method =RequestMethod.GET )
    public String restful19(@PathVariable String name) {
        System.out.println("9 " + name);
        return name;
    }


}
