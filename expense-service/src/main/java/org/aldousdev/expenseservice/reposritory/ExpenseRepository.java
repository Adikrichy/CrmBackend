package org.aldousdev.expenseservice.reposritory;

import org.aldousdev.expenseservice.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List <Expense> findByUserId(Long userId);
}
