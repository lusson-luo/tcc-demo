package cc.shixicheng.orderserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cc.shixicheng.orderserver.model.LockerStorage;

@FeignClient(value = "storage-server")
public interface StorageRemoteService {

    @RequestMapping(value = "try-put", method= RequestMethod.POST)
    String tryPut(@RequestBody LockerStorage storage);

    @RequestMapping(value = "commit", method= RequestMethod.POST)
    String commit(@RequestBody LockerStorage storage);

    @RequestMapping(value = "cancel", method= RequestMethod.POST)
    String cancel(@RequestBody LockerStorage storage);
}
