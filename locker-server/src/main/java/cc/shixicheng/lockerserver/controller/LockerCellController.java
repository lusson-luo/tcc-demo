package cc.shixicheng.lockerserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cc.shixicheng.lockerserver.model.LockerCell;
import cc.shixicheng.lockerserver.service.LockerCellService;

@RestController
public class LockerCellController {

    @Autowired
    private LockerCellService lockerCellService;

    @RequestMapping("/try-put")
    public String tryPut(@RequestBody LockerCell cell) {
        return lockerCellService.tryPut(cell);
    }

    // 最好做幂等处理
    @RequestMapping("/commit")
    public String commit(@RequestBody LockerCell cell) {
        return lockerCellService.commit(cell);
    }

}
