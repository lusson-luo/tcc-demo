package cc.shixicheng.storageserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import cc.shixicheng.storageserver.model.LockerStorage;
import cc.shixicheng.storageserver.repository.LockerStorageRepository;

@Service
public class StorageService {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    @Autowired
    private LockerStorageRepository storageRepository;

    public String tryPut(LockerStorage storage) {
        Optional<LockerStorage> storageOptional = storageRepository.findByLockerCodeAndCellCodeAndSn(storage.getLockerCode(), storage.getCellCode(), storage.getSn());
        if (!storageOptional.isPresent()) {
            return FAILED;
        }
        storage = storageOptional.get();
        if (storage.getUsableInventory() != null || storage.getUsableInventory() == 0) {
            return FAILED;
        }
        storage.setLockedInventory(storage.getLockedInventory() == null ? 1 : storage.getLockedInventory() + 1);
        storage.setUsableInventory(storage.getUsableInventory() == null ? 29 : storage.getUsableInventory() - 1);
        //实际应该加version字段来做乐观锁，不过是demo，取消了
        try {
            storageRepository.save(storage);
            return SUCCESS;
        } catch (Exception ex) {
            return FAILED;
        }
    }

    public String commit(LockerStorage storage) {
        Optional<LockerStorage> storageOptional = storageRepository.findByLockerCodeAndCellCodeAndSn(storage.getLockerCode(), storage.getCellCode(), storage.getSn());
        if (!storageOptional.isPresent()) {
            return FAILED;
        }
        storage = storageOptional.get();
        storage.setLockedInventory(storage.getLockedInventory() - 1);
        storage.setInventory(storage.getInventory() + 1);
        //实际应该加version字段来做乐观锁，不过是demo，取消了
        try {
            storageRepository.save(storage);
            return SUCCESS;
        } catch (Exception ex) {
            return FAILED;
        }
    }
}
