package cc.shixicheng.orderserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import cc.shixicheng.orderserver.model.Greeting;

@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("")
    public Greeting greeting() {
        return new Greeting(counter.incrementAndGet(), "order-server run success");
    }
}
