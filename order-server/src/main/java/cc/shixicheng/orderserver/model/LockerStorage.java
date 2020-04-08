package cc.shixicheng.orderserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockerStorage {
    private long id;
    private String lockerCode;
    private String cellCode;
    private String sn;
    private Integer inventory;
    private Integer lockedInventory;
    private Integer usableInventory;
    private String handleOrder;
}
