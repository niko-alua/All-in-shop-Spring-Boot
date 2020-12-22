package kz.springboot.javaee.hometask7.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bought_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShopItem shopItem;

    @Column(name = "amount")
    private int amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "buy_date",
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date buyDate;
}
