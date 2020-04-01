package cc.shixicheng.orderserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import cc.shixicheng.orderserver.model.MaterialOrder;

@Repository
public interface MaterialOrderRepository extends JpaRepository<MaterialOrder, Long> {

    Optional<MaterialOrder> findByOrderNo(String orderNo);

}
