package com.todoapp.todoapp.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;





@Getter
@Setter
@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Column
    private String status;

    @Column
    private String description;

    @Column
    private Integer userId;

}
