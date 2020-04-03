package cc.shixicheng.lockerserver.model;

import org.springframework.data.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "locker_cell")
public class LockerCell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "locker_code")
    private String lockerCode;
    @Column(name = "cell_code")
    private String cellCode;
    @Column(name = "cell_status")
    private String cellStatus;
    @Column(name = "pre_status")
    private String preStatus;
    @Version
    @Column(name = "version")
    private int version = 0;
}
