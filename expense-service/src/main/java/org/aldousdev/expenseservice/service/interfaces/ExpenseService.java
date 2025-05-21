package org.aldousdev.expenseservice.service.interfaces;

import org.aldousdev.expenseservice.model.Expense;

import java.util.List;

public interface ExpenseService {
    Expense createExpense(Expense expense);

    List<Expense> getExpensesByUserId(Long userId);

    Expense updateExpense(Long id,Expense expense);

    void deleteExpense(Long id);

    Expense getExpenseById(Long id);



}
