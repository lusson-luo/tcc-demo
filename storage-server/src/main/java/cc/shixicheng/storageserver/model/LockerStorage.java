package cc.shixicheng.storageserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "locker_storage")
public class LockerStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "locker_code")
    private String lockerCode;
    @Column(name = "cell_code")
    private String cellCode;
    @Column(name = "sn")
    private String sn;
    private Integer inventory;
    @Column(name = "locked_inventory")
    private Integer lockedInventory;
    @Column(name = "usable_inventory")
    private Integer usableInventory;
}
