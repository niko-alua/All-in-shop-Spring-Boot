package kz.springboot.javaee.hometask7.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Lob
    @Column(name = "comment", length = 512)
    String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_date",
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    Date addedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    ShopItem item;

    @ManyToOne(fetch = FetchType.EAGER)
    ShopUser author;
}
