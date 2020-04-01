package cc.shixicheng.orderserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "material_order")
public class MaterialOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "biz_type")
    private String bizType;
    @Column(name = "sn")
    private String sn;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "pre_status")
    private String preStatus;
}
