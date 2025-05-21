package org.aldousdev.expenseservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.aldousdev.expenseservice.model.Expense;
import org.aldousdev.expenseservice.reposritory.ExpenseRepository;
import org.aldousdev.expenseservice.service.interfaces.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    @Override
    public Expense createExpense(Expense expense) {
        // In this place we just save it
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public Expense updateExpense(Long id, Expense expense){
        // Find with id
        Expense exp = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

        // 2 Update the data
        exp.setAmount(expense.getAmount());
        exp.setTitle(expense.getTitle());
        exp.setExpenceCategory(expense.getExpenceCategory());
        exp.setCurrency(expense.getCurrency());

        return expenseRepository.save(exp);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public Expense getExpenseById(Long id) {
        expenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Expense not found"));
        return expenseRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Expense not found"));
    }

}
