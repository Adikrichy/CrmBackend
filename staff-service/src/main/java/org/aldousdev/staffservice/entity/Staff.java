package org.aldousdev.staffservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate hireDate;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


}
