package cc.shixicheng.storageserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import cc.shixicheng.storageserver.model.Greeting;
import cc.shixicheng.storageserver.model.LockerStorage;

@Repository
public interface LockerStorageRepository extends JpaRepository<LockerStorage, Long> {

    Optional<LockerStorage> findByLockerCodeAndCellCodeAndSn(String lockerCode, String cellCode, String sn);

    @Transactional
    @Modifying
    @Query("update locker_storage ls set ls.inventory = :#{#storage.inventory}, ls.lockedInventory = :#{#storage.lockedInventory}, " +
            "ls.usableInventory = :#{#storage.usableInventory}, ls.version = :#{#storage.version} + 1, ls.handleOrder = null  where ls.id = :#{#storage.id} and " +
            "ls.version = :#{#storage.version} and ls.handleOrder = :#{#storage.handleOrder}")
    int updateAndUnlock(@Param(value = "storage") LockerStorage storage);

    @Transactional
    @Modifying
    @Query("update locker_storage ls set ls.inventory = :#{#storage.inventory}, ls.lockedInventory = :#{#storage.lockedInventory}, " +
            "ls.usableInventory = :#{#storage.usableInventory}, ls.handleOrder = :#{#storage.handleOrder}, ls.version = :#{#storage.version} + 1 " +
            "where ls.id = :#{#storage.id} and ls.version = :#{#storage.version} ")
    int updateAndLock(@Param(value = "storage") LockerStorage storage);

}
