package cc.shixicheng.storageserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import cc.shixicheng.storageserver.model.Greeting;
import cc.shixicheng.storageserver.model.LockerStorage;
import cc.shixicheng.storageserver.service.StorageService;

@RestController
public class StorageController {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private StorageService storageService;

    @GetMapping("")
    public Greeting greeting() {
        return new Greeting(counter.incrementAndGet(), "storage-server run success");
    }

    @PostMapping("/try-put")
    public String tryPut(@RequestBody LockerStorage storage) {
        return storageService.tryPut(storage);
    }

    // todo: 做幂等
    @PostMapping("/commit")
    public String commit(@RequestBody LockerStorage storage) {
        return storageService.commit(storage);
    }

    // todo: 做幂等接口
    @PostMapping("/cancel")
    public String cancel(@RequestBody LockerStorage storage) {
        return storageService.cancel(storage);
    }
}
