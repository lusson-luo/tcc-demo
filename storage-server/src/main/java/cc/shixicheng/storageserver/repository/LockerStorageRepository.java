package cc.shixicheng.storageserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import cc.shixicheng.storageserver.model.Greeting;
import cc.shixicheng.storageserver.model.LockerStorage;

@Repository
public interface LockerStorageRepository extends JpaRepository<LockerStorage, Long> {

    public Optional<LockerStorage> findByLockerCodeAndCellCodeAndSn(String lockerCode, String cellCode, String sn);
}
