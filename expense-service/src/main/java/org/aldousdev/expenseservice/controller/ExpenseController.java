package org.aldousdev.expenseservice.controller;

import org.aldousdev.expenseservice.model.Expense;
import org.aldousdev.expenseservice.service.interfaces.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        Expense saved = expenseService.createExpense(expense);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Expense>> getExpensesByUserId(@PathVariable Long id) {
         List<Expense> expenses = expenseService.getExpensesByUserId(id);
         return ResponseEntity.ok(expenses);
    }

    @GetMapping("{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id){
        Expense expenseid = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expenseid);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<Expense> updateExpense(@Valid @RequestBody Expense expense, @PathVariable Long id) {
        Expense updateExpense = expenseService.updateExpense(id, expense);
        return ResponseEntity.ok(updateExpense);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

}
