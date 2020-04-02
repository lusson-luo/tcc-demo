package cc.shixicheng.lockerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import cc.shixicheng.lockerserver.model.LockerCell;
import cc.shixicheng.lockerserver.repository.LockerCellRepository;

@Service
public class LockerCellService {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    @Autowired
    private LockerCellRepository lockerCellRepository;

    public String tryPut(LockerCell cell) {
        Optional<LockerCell> lockerCell = lockerCellRepository.findByLockerCodeAndCellCode(cell.getLockerCode(), cell.getCellCode());
        if (!lockerCell.isPresent()) {
            return FAILED;
        }
        lockerCell.get().setPreStatus("putting");
        try {
            lockerCellRepository.save(lockerCell.get());
            return SUCCESS;
        } catch (Exception ex) {
            return FAILED;
        }
    }

    public String commit(LockerCell cell) {
        Optional<LockerCell> lockerCell = lockerCellRepository.findByLockerCodeAndCellCode(cell.getLockerCode(), cell.getCellCode());
        if (!lockerCell.isPresent()) {
            return FAILED;
        }
        lockerCell.get().setPreStatus(null);
        lockerCell.get().setCellStatus("usable");
        try {
            // todo: 应改为version的乐观锁
            lockerCellRepository.save(lockerCell.get());
            return SUCCESS;
        } catch (Exception ex) {
            return FAILED;
        }
    }

    public String cancel(LockerCell cell) {
        Optional<LockerCell> lockerCell = lockerCellRepository.findByLockerCodeAndCellCode(cell.getLockerCode(), cell.getCellCode());
        if (!lockerCell.isPresent()) {
            return FAILED;
        }
        lockerCell.get().setPreStatus(null);
        try {
            // todo: 应改为version的乐观锁
            lockerCellRepository.save(lockerCell.get());
            return SUCCESS;
        } catch (Exception ex) {
            return FAILED;
        }
    }
}
