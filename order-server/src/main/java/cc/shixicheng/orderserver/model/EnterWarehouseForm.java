package cc.shixicheng.orderserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterWarehouseForm {
    private String orderNo;
    private String lockerCode;
    private String cellCode;
    private String sn;
}
