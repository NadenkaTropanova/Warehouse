package com.trade.repository;

import com.trade.domain.Trade;
import com.trade.domain.Type;
import com.trade.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUser(User user);
    List<Trade> findTradesByProductNameAndTypeAndUser(String name, Type type, User user);
}
