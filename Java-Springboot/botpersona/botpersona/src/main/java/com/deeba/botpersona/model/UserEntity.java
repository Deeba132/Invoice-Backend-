package com.deeba.botpersona.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;

    @ManyToOne //Specifies that many ItemEntity objects can be associated with one LoginEntity
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column in user_entity table
    private LoginEntity owner; // ✅ reference to full login entity object
}
