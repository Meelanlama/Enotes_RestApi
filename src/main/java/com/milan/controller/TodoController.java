package com.milan.controller;

import com.milan.dto.TodoDto;
import com.milan.endpoint.TodoEndpoint;
import com.milan.service.TodoService;
import com.milan.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController implements TodoEndpoint {

    private final TodoService todoService;

    @Override
    public ResponseEntity<?> saveTodo(TodoDto todo) throws Exception {
        Boolean saveTodo = todoService.saveTodo(todo);
        if (saveTodo) {
            return CommonUtil.createBuildResponseMessage("Todo Saved Success", HttpStatus.CREATED);
        } else {
            return CommonUtil.createErrorResponseMessage("Todo not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getTodoById(Integer id) throws Exception {
        TodoDto todo = todoService.getTodoById(id);
        return CommonUtil.createBuildResponse(todo, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getAllTodoByUser(){
        List<TodoDto> todoList = todoService.getTodoByUser();
        if (CollectionUtils.isEmpty(todoList)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(todoList, HttpStatus.OK);
    }
}
