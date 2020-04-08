package cc.shixicheng.lockerserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.shixicheng.lockerserver.model.Greeting;
import cc.shixicheng.lockerserver.model.LockerCell;
import cc.shixicheng.lockerserver.service.LockerCellService;

@RestController
public class LockerCellController {

    @Autowired
    private LockerCellService lockerCellService;

    @GetMapping("")
    public Greeting greeting() {
        return new Greeting("locker-server run success");
    }

    @RequestMapping("/try-put")
    public String tryPut(@RequestBody LockerCell cell) {
        return lockerCellService.tryPut(cell);
    }

    //todo: 要做幂等处理
    @RequestMapping("/commit")
    public String commit(@RequestBody LockerCell cell) {
        return lockerCellService.commit(cell);
    }

    //todo: 要做幂等处理
    @RequestMapping("/cancel")
    public String cancel(@RequestBody LockerCell cell) {
        return lockerCellService.cancel(cell);
    }
}
