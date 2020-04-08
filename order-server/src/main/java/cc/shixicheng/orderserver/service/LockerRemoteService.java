package cc.shixicheng.orderserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cc.shixicheng.orderserver.model.LockerCell;

@FeignClient(value = "locker-server")
public interface LockerRemoteService {

    @RequestMapping(value = "try-put", method= RequestMethod.POST)
    String tryPut(@RequestBody LockerCell lockerCell);

    @RequestMapping(value = "commit", method= RequestMethod.POST)
    String commit(@RequestBody LockerCell lockerCell);

    @RequestMapping(value = "cancel", method= RequestMethod.POST)
    String cancel(@RequestBody LockerCell lockerCell);

}
