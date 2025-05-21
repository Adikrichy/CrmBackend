package org.aldousdev.expenseservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;

    @Column
    @Enumerated (EnumType.STRING)
    private Currency currency;

    @Column
    @Enumerated(EnumType.STRING)
    private ExpenceCategory expenceCategory;

    private LocalDateTime data;

    private Long userId;





}
