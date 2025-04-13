package com.milan.endpoint;

import com.milan.dto.TodoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.milan.util.Constants.ROLE_USER;

@Tag(name = "TODO APIs", description = "All the Todo Operation APIs")
@RequestMapping("/api/v1/todo")
public interface TodoEndpoint {

    @Operation(summary = "Save Todo by user", tags = { "Notes" }, description = "Save Todo")
    @PostMapping("/")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> saveTodo(@RequestBody TodoDto todo) throws Exception;

    @Operation(summary = "GET Todo by id", tags = { "Notes" }, description = "GET Todo")
    @GetMapping("/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception;

    @Operation(summary = "GET ALL TODO IN LIST BY USER", tags = { "Notes" }, description = "GET Todo")
    @GetMapping("/list")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getAllTodoByUser();
}
