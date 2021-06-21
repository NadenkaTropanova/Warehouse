package com.trade.service;

import com.trade.domain.Trade;
import com.trade.domain.Type;
import com.trade.repository.TradeRepository;
import com.trade.request.TradeRequest;
import com.trade.service.exception.TradeNotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {
    private final UserService userService;
    private final TradeRepository tradeRepository;

    public TradeService(UserService userService, TradeRepository tradeRepository) {
        this.userService = userService;
        this.tradeRepository = tradeRepository;
    }

    @Transactional
    public List<Trade> getTradesForCurrentUser() {
        return tradeRepository.findByUser(userService.getCurrentUser());
    }

    @Transactional
    public void addTrade(@NonNull TradeRequest request) {
        Trade trade = new Trade();
        trade.setId(null);
        trade.setTime(request.getTime());
        trade.setType(request.getType());
        trade.setCompanyName(request.getCompanyName());
        trade.setProductName(request.getProductName());
        trade.setAmount(request.getAmount());
        trade.setPrice(request.getPrice());
        trade.setDate(new Date());
        trade.setUser(userService.getCurrentUser());

        tradeRepository.save(trade);
    }

    @Transactional
    public void deleteTrade(@NonNull Long id) throws TradeNotFoundException {
        if (tradeRepository.existsById(id)) {
            tradeRepository.deleteById(id);
        } else {
            throw new TradeNotFoundException();
        }
    }

    @Transactional
    public long getProductAmount(String name, Type type) {
        List<Trade> trades = tradeRepository.
                findTradesByProductNameAndTypeAndUser(name, type, userService.getCurrentUser());

        return trades.stream().mapToLong(Trade::getAmount).sum();
    }
}
