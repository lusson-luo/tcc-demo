package cc.shixicheng.lockerserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import cc.shixicheng.lockerserver.model.LockerCell;
import cc.shixicheng.lockerserver.repository.LockerCellRepository;

@Service
public class LockerCellService {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    public static final Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private LockerCellRepository lockerCellRepository;

    public String tryPut(LockerCell cell) {
        Optional<LockerCell> lockerCell = lockerCellRepository.findByLockerCodeAndCellCode(cell.getLockerCode(), cell.getCellCode());
        if (!lockerCell.isPresent()) {
            log.error("cell try操作执行失败，未找到对应的lockerCell，参数cell: ", cell);
            return FAILED;
        }
        lockerCell.get().setPreStatus("putting");
        try {
            lockerCellRepository.save(lockerCell.get());
            log.info("cell try操作修改数据成功");
            return SUCCESS;
        } catch (Exception ex) {
            log.error("cell try操作执行失败.", ex);
            return FAILED;
        }
    }

    public String commit(LockerCell cell) {
        Optional<LockerCell> lockerCell = lockerCellRepository.findByLockerCodeAndCellCode(cell.getLockerCode(), cell.getCellCode());
        if (!lockerCell.isPresent()) {
            log.error("cell try操作执行失败，未找到对应的lockerCell，参数cell: ", cell);
            return FAILED;
        }
        lockerCell.get().setPreStatus(null);
        lockerCell.get().setCellStatus("usable");
        try {
            lockerCellRepository.save(lockerCell.get());
            log.info("cell commit操作修改数据成功");
            return SUCCESS;
        } catch (Exception ex) {
            log.error("cell commit操作执行失败.", ex);
            return FAILED;
        }
    }

    public String cancel(LockerCell cell) {
        Optional<LockerCell> lockerCell = lockerCellRepository.findByLockerCodeAndCellCode(cell.getLockerCode(), cell.getCellCode());
        if (!lockerCell.isPresent()) {
            log.error("cell cancel操作执行失败，未找到对应的lockerCell，参数cell: ", cell);
            return FAILED;
        }
        lockerCell.get().setPreStatus(null);
        try {
            lockerCellRepository.save(lockerCell.get());
            log.info("cell cancel操作修改数据成功");
            return SUCCESS;
        } catch (Exception ex) {
            log.error("cell cancel操作执行失败，未找到对应的lockerCell，参数cell: ", cell);
            return FAILED;
        }
    }
}
