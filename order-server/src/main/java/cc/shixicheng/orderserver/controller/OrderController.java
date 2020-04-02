package cc.shixicheng.orderserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import cc.shixicheng.orderserver.model.EnterWarehouseForm;
import cc.shixicheng.orderserver.model.Greeting;
import cc.shixicheng.orderserver.service.OrderService;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public Greeting greeting() {
        return new Greeting( "order-server run success");
    }

    @GetMapping("/commit")
    public String commit() {
        EnterWarehouseForm form = new EnterWarehouseForm("001", "0100001", "A01", "sn20200401-08G");
        return orderService.enterWarehouse(form);
    }

    @GetMapping("/cancel")
    public String cancel() {
        EnterWarehouseForm form = new EnterWarehouseForm("001", "0100002", "A01", "sn20200401-08G");
        return orderService.enterWarehouse(form);
    }
}
