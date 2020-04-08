package cc.shixicheng.storageserver.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import cc.shixicheng.storageserver.model.LockerStorage;

@SpringBootTest
public class LockerStorageRepositoryTest{
    @Autowired
    private LockerStorageRepository lockerStorageRepository;

    @Test
    public void list() {
        Assert.notEmpty(lockerStorageRepository.findAll(), "lockerStorage数据不为空");
    }
    
}
