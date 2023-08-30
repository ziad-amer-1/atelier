package com.atelier.repository;

import com.atelier.entity.AppUser;
import com.atelier.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<UserOrder, Long> {


    @Query("""
        SELECT o FROM UserOrder o WHERE
        (?1 IS NULL OR to_char(o.date, 'yyyy-MM-dd') = ?1)
        AND
        (?2 IS NULL OR o.status = ?2)
    """)
    List<UserOrder> getAllOrdersInSpecificDate(String localDate, String status);

}
