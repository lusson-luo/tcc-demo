package cc.shixicheng.storageserver.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import cc.shixicheng.storageserver.model.LockerStorage;
import cc.shixicheng.storageserver.repository.LockerStorageRepository;

// 对于有数量修改的数据，最好还是锁住这条数据严谨一点
@Service
public class StorageService {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    public static final Logger log = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    private LockerStorageRepository storageRepository;

    public String tryPut(LockerStorage storageForm) {
        Optional<LockerStorage> storageOptional = storageRepository.findByLockerCodeAndCellCodeAndSn(storageForm.getLockerCode(), storageForm.getCellCode(), storageForm.getSn());
        if (!storageOptional.isPresent()) {
            log.error("storage try操作执行失败，未找到对应的storage，参数storageForm: " + storageForm.toString());
            return FAILED;
        }
        LockerStorage storage = storageOptional.get();
        if (storage.getUsableInventory() != null && storage.getUsableInventory() == 0) {
            log.error("storage try操作执行失败，storage可用库存不足，storage: " + storage.toString());
            return FAILED;
        }
        if (StringUtils.isNotBlank(storage.getHandleOrder())) {
            log.error("storage try操作执行失败，storage被其他订单锁住，storage: " + storage.toString());
            return FAILED;
        }
        storage.setLockedInventory(storage.getLockedInventory() == null ? 1 : storage.getLockedInventory() + 1);
        storage.setUsableInventory(storage.getUsableInventory() == null ? 29 : storage.getUsableInventory() - 1);
        storage.setHandleOrder(storageForm.getHandleOrder());
        try {
            int runState = storageRepository.updateAndLock(storage);
            if (runState != 0) {
                log.info("storage try操作修改数据成功");
                return SUCCESS;
            }
            log.error("storage try操作修改数据失败，查询条件不符合, storage参数: " + storage.toString()+"; runState " + runState);
            return FAILED;
        } catch (Exception ex) {
            log.error("storage try操作执行失败.", ex);
            return FAILED;
        }
    }

    public String commit(LockerStorage storageForm) {
        Optional<LockerStorage> storageOptional = storageRepository.findByLockerCodeAndCellCodeAndSn(storageForm.getLockerCode(), storageForm.getCellCode(), storageForm.getSn());
        if (!storageOptional.isPresent()) {
            log.error("storage commit操作执行失败，未找到对应的storage，参数storageForm: "+storageForm.toString());
            return FAILED;
        }

        LockerStorage storage = storageOptional.get();
        if (!Objects.equals(storage.getHandleOrder(), storageForm.getHandleOrder())) {
            log.error("storage commit操作执行失败，storage未被当前订单锁住，storage: "+storage.toString());
            return FAILED;
        }
        storage.setLockedInventory(storage.getLockedInventory() - 1);
        storage.setInventory(storage.getInventory() + 1);
        storage.setHandleOrder(storageForm.getHandleOrder());
        try {
            int runState = storageRepository.updateAndUnlock(storage);
            if (runState != 0) {
                log.info("storage commit操作修改数据成功");
                return SUCCESS;
            }
            log.error("storage commit操作修改数据失败，查询条件不符合, storage参数: " + storage.toString()+"; runState " + runState);
            return FAILED;
        } catch (Exception ex) {
            log.error("storage commit操作执行失败.", ex);
            return FAILED;
        }
    }

    public String cancel(LockerStorage storageForm) {
        Optional<LockerStorage> storageOptional = storageRepository.findByLockerCodeAndCellCodeAndSn(storageForm.getLockerCode(), storageForm.getCellCode(), storageForm.getSn());
        if (!storageOptional.isPresent()) {
            log.error("storage cancel操作执行失败，未找到对应的storage，参数storageForm: "+storageForm.toString());
            return FAILED;
        }
        LockerStorage storage = storageOptional.get();
        if (!Objects.equals(storage.getHandleOrder(), storageForm.getHandleOrder())) {
            log.error("storage cancel操作执行失败，storage未被当前订单锁住，storage: "+storage.toString());
            return FAILED;
        }
        storage.setLockedInventory(0);
        storage.setUsableInventory(storage.getCapacity() - storage.getInventory());
        storage.setHandleOrder(storageForm.getHandleOrder());
        try {
            int runState = storageRepository.updateAndUnlock(storage);
            if (runState != 0) {
                log.info("storage cancel操作修改数据成功");
                return SUCCESS;
            }
            log.error("storage cancel操作修改数据失败，查询条件不符合, storage参数: " + storage.toString()+"; runState " + runState);
            return FAILED;
        } catch (Exception ex) {
            log.error("storage cancel操作执行失败.", ex);
            return FAILED;
        }
    }
}
