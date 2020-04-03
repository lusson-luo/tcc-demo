package cc.shixicheng.orderserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import cc.shixicheng.orderserver.model.EnterWarehouseForm;
import cc.shixicheng.orderserver.model.LockerCell;
import cc.shixicheng.orderserver.model.LockerStorage;
import cc.shixicheng.orderserver.model.MaterialOrder;
import cc.shixicheng.orderserver.repository.MaterialOrderRepository;

@Service
public class OrderService {

    public static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public static final AtomicInteger atomicInteger = new AtomicInteger();
    public static final int RUN_THRESHOLD = 5;
    public static final String PRE_PUT = "putting";
    public static final String ALREADY_PUT = "PUT";
    public static final String SUCCESS = "success";

    @Autowired
    private MaterialOrderRepository orderRepository;
    @Autowired
    private LockerRemoteService lockerRemoteService;
    @Autowired
    private StorageRemoteService storageRemoteService;

    // todo: try执行的时，如果遇到部分服务没有执行成功，到cancel时，所有服务都会执行还原任务，就会导致没有执行try服务的也还原了，这个还原过程如果时 xx=xx-1这种类型，数据就会出错。
    // 想到一种方式解决，cancel不能用xx=xx-1这种方式，只能通过不变的数据算出来，且这个事务执行，就会锁住这条记录，其他事务不能修改，设置handleOrder。
    public String enterWarehouse(EnterWarehouseForm form) {
        if (tryEnter(form)) {
            commit(form);
            return "commit success";
        } else {
            cancel(form);
            return "cancel success";
        }
    }

    // tcc的try，一旦失败直接走cancel
    public boolean tryEnter(EnterWarehouseForm form) {
        Optional<MaterialOrder> order = orderRepository.findByOrderNo(form.getOrderNo());
        if (!order.isPresent()) {
            logger.error("try执行失败，未查询到order");
            return false;
        }
        order.get().setPreStatus(PRE_PUT);
        //todo: 改乐观锁，且判断状态
        orderRepository.save(order.get());
        try {
            LockerCell lockerCell = new LockerCell(0, form.getLockerCode(), form.getCellCode(), null, null);
            LockerStorage storage = new LockerStorage(0, form.getLockerCode(), form.getCellCode(), form.getSn(), null, null, null, form.getOrderNo());
            String lockerRes = lockerRemoteService.tryPut(lockerCell);
            logger.info("locker远程执行结果：" + lockerRes);
            String storageRes = storageRemoteService.tryPut(storage);
            logger.info("storage远程执行结果：" + storageRes);
            return SUCCESS.equals(lockerRes) && SUCCESS.equals(storageRes);
//            return SUCCESS.equals(lockerRemoteService.tryPut(lockerCell)) && SUCCESS.equals(storageRemoteService.tryPut(storage));
        } catch (Exception ex) {
            logger.error("调用远程服务失败", ex);
            return false;
        }
    }

    // 失败应该重复执行，直到失败次数达到阀值才不执行，生存工单，人肉处理
    public void commit(EnterWarehouseForm form) {
        Optional<MaterialOrder> order = orderRepository.findByOrderNo(form.getOrderNo());
        if (!order.isPresent()) {
            commitAgain(form);
            return;
        }
        order.get().setPreStatus(null);
        order.get().setOrderStatus(ALREADY_PUT);
        orderRepository.save(order.get());

        try {
            LockerCell lockerCell = new LockerCell(0, form.getLockerCode(), form.getCellCode(), null, null);
            LockerStorage storage = new LockerStorage(0, form.getLockerCode(), form.getCellCode(), form.getSn(), null, null, null, form.getOrderNo());
            if (!SUCCESS.equals(lockerRemoteService.commit(lockerCell)) || !SUCCESS.equals(storageRemoteService.commit(storage))) {
                commitAgain(form);
            }
        } catch (Exception ex) {
            logger.error("commit执行失败第" + (atomicInteger.get() + 1) + "次", ex);
            commitAgain(form);
        }
    }

    @Async
    public void commitAgain(EnterWarehouseForm form) {
        if (atomicInteger.incrementAndGet() > RUN_THRESHOLD) {
            System.out.println("commit失败，请人工处理，orderNo:" + form.getOrderNo());
            return;
        }
        commit(form);
        return;
    }

    // 失败应该重复执行，直到失败次数达到阀值才不执行，生存工单，人肉处理
    public void cancel(EnterWarehouseForm form) {
        Optional<MaterialOrder> order = orderRepository.findByOrderNo(form.getOrderNo());
        if (!order.isPresent()) {
            cancelAgain(form);
            return;
        }
        order.get().setPreStatus(null);
        order.get().setOrderStatus(null);
        orderRepository.save(order.get());
        try {
            LockerCell lockerCell = new LockerCell(0, form.getLockerCode(), form.getCellCode(), null, null);
            LockerStorage storage = new LockerStorage(0, form.getLockerCode(), form.getCellCode(), form.getSn(), null, null, null, form.getOrderNo());
            if (!SUCCESS.equals(lockerRemoteService.cancel(lockerCell)) || !SUCCESS.equals(storageRemoteService.cancel(storage))) {
                cancelAgain(form);
            }
        } catch (Exception ex) {
            logger.error("cancel执行失败第" + (atomicInteger.get() + 1) + "次", ex);
            cancelAgain(form);
        }
    }

    //重复执行的cancel
    @Async
    public void cancelAgain(EnterWarehouseForm form) {
        if (atomicInteger.incrementAndGet() > RUN_THRESHOLD) {
            System.out.println("cancel失败，请人工处理，orderNo:" + form.getOrderNo());
            return;
        }
        cancel(form);
        return;
    }
}
