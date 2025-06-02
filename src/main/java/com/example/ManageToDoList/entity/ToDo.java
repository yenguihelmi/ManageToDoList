package com.example.ManageToDoList.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label ;
    private String description;
    private boolean completed;

    public ToDo(String label, String description, boolean completed) {
        this.label = label;
        this.description = description;
        this.completed = completed;
    }

}
