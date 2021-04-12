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
public class TestController implements TestInter {

    @GetMapping(value = "/test0/{name}", headers = {"servicename=test0"})
    public Response<String> test0(@PathVariable String name) {
        System.out.println("test0 " + name);
        return Response.ok(name);
    }

    @GetMapping(value = "/test1/11/11/{name}")
    public Response<String> test111(@PathVariable String name) {
        System.out.println("test1 " + name);
        return Response.ok(name);
    }

    @GetMapping(value = "/test1/11/22/{name}")
    public Response<String> test122(@PathVariable String name) {
        System.out.println("test1 " + name);
        return Response.ok(name);
    }

    @GetMapping(value = "/test2")
    public Response<String> test2(@RequestParam String name) {
        System.out.println("test2 " + name);
        return Response.ok(name);
    }

    //=====================================================================//
    @GetMapping(value = "restful/{name}")
    public String restful(@PathVariable String name) {
        System.out.println("restful " + name);
        return name;
    }

    @GetMapping(value = "norestful")
    public String norestful(@RequestParam String name) {
        System.out.println("norestful " + name);
        return name;
    }


    private static List<String> strings = List.of("1");

    @PostMapping(value = "test1")
    public String norestful1(@RequestBody User user) {
        System.out.println("user " + user.toString());
        List<String> habits = user.getHabits();
        if (habits == null) {
            user.setHabits(strings);
        } else {
            if (!habits.contains("1")) habits.add("1");
            user.setHabits(habits);
        }
        addUser(user);

        return user.toString();
    }

}
