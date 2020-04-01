package cc.shixicheng.lockerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import cc.shixicheng.lockerserver.model.LockerCell;

@Repository
public interface LockerCellRepository extends JpaRepository<LockerCell, Long> {

    Optional<LockerCell> findByLockerCodeAndCellCode(String lockerCode, String cellCode);
}
