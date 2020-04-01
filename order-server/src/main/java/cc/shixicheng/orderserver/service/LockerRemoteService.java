package cc.shixicheng.orderserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cc.shixicheng.orderserver.model.LockerCell;

@FeignClient(value = "locker")
public interface LockerRemoteService {

    @RequestMapping(value = "locker-server/try-put", method= RequestMethod.POST)
    String tryPut(@RequestBody LockerCell lockerCell);

    @RequestMapping(value = "locker-server/commit", method= RequestMethod.POST)
    String commit(@RequestBody LockerCell lockerCell);

    @RequestMapping(value = "locker-server/cancel", method= RequestMethod.POST)
    String cancel(@RequestBody LockerCell lockerCell);

}
