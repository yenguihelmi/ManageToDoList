package com.example.ManageToDoList.controller;

import com.example.ManageToDoList.entity.ToDo;
import com.example.ManageToDoList.service.ToDoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ToDoControllerTest {

    @Mock
    private ToDoService toDoService;

    @InjectMocks
    private ToDoController toDoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getToDoListShouldReturnAllToDos() {
        List<ToDo> mockToDos = List.of(
                new ToDo("TO-DO-1", "Test 1", true),
                new ToDo("TO-DO-2", "Test 2", false)
        );
        when(toDoService.getAllToDo()).thenReturn(mockToDos);

        ResponseEntity<List<ToDo>> response = toDoController.getToDoList();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .hasSize(2)
                .extracting(ToDo::getLabel)
                .containsExactly("TO-DO-1", "TO-DO-2");
        verify(toDoService).getAllToDo();
    }

    @Test
    void addToDoShouldCreateToDosAndReturn201() {
        ToDo newToDo = new ToDo("New-1", "Desc 1", false);
        when(toDoService.createAllToDo(anyList())).thenReturn(List.of(newToDo));

        ResponseEntity<List<ToDo>> response = toDoController.addToDo(List.of(newToDo));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody())
                .singleElement()
                .satisfies(todo -> {
                    assertThat(todo.getLabel()).isEqualTo("New-1");
                    assertThat(todo.isCompleted()).isFalse();
                });
        verify(toDoService).createAllToDo(anyList());
    }

    @Test
    void getToDoByIdShouldReturnToDoWhenExists() {
        ToDo mockToDo = new ToDo("TO-DO-1", "Test", true);
        when(toDoService.getToDoById(1L)).thenReturn(mockToDo);

        ToDo result = toDoController.getToDoById(1L);

        assertThat(result)
                .isNotNull()
                .extracting(ToDo::getLabel, ToDo::isCompleted)
                .containsExactly("TO-DO-1", true);
    }

    @Test
    void getCompletedToDosShouldReturnOnlyCompleted() {
        ToDo completedToDo = new ToDo("Done-1", "Completed", true);
        when(toDoService.getTasksByStatus(true)).thenReturn(List.of(completedToDo));

        List<ToDo> result = toDoController.getCompletedToDos();

        assertThat(result)
                .hasSize(1)
                .allSatisfy(todo -> assertThat(todo.isCompleted()).isTrue());
    }

    @Test
    void updateToDoStatusShouldUpdateAndReturnToDo() {
        ToDo updatedToDo = new ToDo("Updated", "Desc", true);
        when(toDoService.updateTaskStatus(1L, true)).thenReturn(updatedToDo);

        ToDo result = toDoController.updateToDoStatus(1L, true);

        assertThat(result)
                .extracting(ToDo::isCompleted)
                .isEqualTo(true);
    }
}