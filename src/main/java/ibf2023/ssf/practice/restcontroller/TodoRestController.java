package ibf2023.ssf.practice.restcontroller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.service.TodoService;

@RestController
@RequestMapping("api/todo")
public class TodoRestController {

    @Autowired
    TodoService todoService;

    @GetMapping
    public ResponseEntity<List<Todo>> getTodoList() {
        try {
            return ResponseEntity.ok(todoService.getToDoList());
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTodoById(@PathVariable("id") String id) {
        if (todoService.getTodoById(id) != null) {
            return ResponseEntity.ok(todoService.getTodoById(id));
        } else {
            return ResponseEntity.badRequest().body("ID: " + id + " not exist");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Todo> addNewTodo(@RequestBody Todo todo) {
        try {
            todo.setId(UUID.randomUUID().toString());
            todo.setCreatedAt(new Date());
            todo.setUpdatedAt(new Date());
            todoService.createTodo(todo);
            return ResponseEntity.ok().body(todo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(
        @PathVariable("id") String id, 
        @RequestBody Todo todo) {

        try {
            // SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            todo.setId(id);
            todo.setCreatedAt(new Date()); // wrong, but i leave it first
            todo.setUpdatedAt(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        return ResponseEntity.ok().body(todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTodo(@PathVariable("id") String id) {
        if (todoService.getTodoById(id) != null) {
            todoService.deleteTodo(todoService.getTodoById(id));
            return ResponseEntity.ok().body("ID: " + id + "is deleted");
        } else {
            return ResponseEntity.badRequest().body("ID: " + id + " not exist");
        }
    }
}
