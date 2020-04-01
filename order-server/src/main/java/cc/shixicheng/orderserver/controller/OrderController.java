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
        String orderNo = "001";
        String lockerCode = "0100001";
        String cellCode="A01";

        EnterWarehouseForm form = new EnterWarehouseForm(orderNo, lockerCode, cellCode, "sn20200401-08G");
        orderService.enterWarehouse(form);
        return "commit success";
    }

    @GetMapping("/cancel")
    public String cancel() {
        String orderNo = "001";
        String lockerCode = "0100002";
        String cellCode="A01";
        orderService.enterWarehouse(orderNo, lockerCode, cellCode);
        return "cancel success";
    }
}
