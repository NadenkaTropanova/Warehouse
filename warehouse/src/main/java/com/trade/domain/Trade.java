package com.trade.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company")
    private String companyName;
    private String time;
    @Column(name = "product")
    private String productName;
    @Enumerated(value = EnumType.STRING)
    private Type type;
    private Integer amount;
    private BigDecimal price;
    @Temporal(value = TemporalType.DATE)
    private Date date;
    @OneToOne
    private User user;
}
