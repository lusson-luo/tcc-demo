package cc.shixicheng.orderserver.service;

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

    public void enterWarehouse(EnterWarehouseForm form) {
        if (tryEnter(form)) {
            commit(form);
        } else {
            cancel(form);
        }
    }

    // tcc的try，一旦失败直接走cancel
    public boolean tryEnter(EnterWarehouseForm form) {
        Optional<MaterialOrder> order = orderRepository.findByOrderNo(form.getOrderNo());
        if (!order.isPresent()) {
            return false;
        }
        order.get().setPreStatus(PRE_PUT);
        //实际应该加version字段来做乐观锁，不过是demo，取消了
        orderRepository.save(order.get());
        try {
            LockerCell lockerCell = new LockerCell(0, form.getLockerCode(), form.getCellCode(), null, null);
            LockerStorage storage = new LockerStorage(0, form.getLockerCode(), form.getCellCode(), form.getSn(), null, null, null);
            return SUCCESS.equals(lockerRemoteService.tryPut(lockerCell)) && SUCCESS.equals(storageRemoteService.tryPut(storage));
        } catch (Exception ex) {
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
            LockerStorage storage = new LockerStorage(0, form.getLockerCode(), form.getCellCode(), form.getSn(), null, null, null);
            if (!SUCCESS.equals(lockerRemoteService.commit(lockerCell)) || !SUCCESS.equals(storageRemoteService.commit(storage))) {
                commitAgain(form);
            }
        } catch (Exception ex) {
            commitAgain(form);
        }
    }

    @Async
    public void commitAgain(EnterWarehouseForm form) {
        if (atomicInteger.incrementAndGet() > RUN_THRESHOLD) {
            if (atomicInteger.incrementAndGet() > RUN_THRESHOLD) {
                System.out.println("commit失败，请人工处理，orderNo:" + form.getOrderNo());
                return;
            }
            commit(form);
            return;
        }
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
            LockerStorage storage = new LockerStorage(0, form.getLockerCode(), form.getCellCode(), form.getSn(), null, null, null);
            if (!SUCCESS.equals(lockerRemoteService.cancel(lockerCell)) || !SUCCESS.equals(storageRemoteService.cancel(storage))) {
                cancelAgain(form);
            }
        } catch (Exception ex) {
            cancelAgain(form);
        }
    }

    //重复执行的cancel
    @Async
    public void cancelAgain(EnterWarehouseForm form) {
        if (atomicInteger.incrementAndGet() > RUN_THRESHOLD) {
            if (atomicInteger.incrementAndGet() > RUN_THRESHOLD) {
                System.out.println("cancel失败，请人工处理，orderNo:" + form.getOrderNo());
                return;
            }
            cancel(form);
            return;
        }
    }
}
