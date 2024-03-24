package ibf2023.ssf.practice.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.service.TodoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping
public class TodoController {
    
    @Autowired
    private TodoService todoService;

    // Get the listing page
    @GetMapping
    public ModelAndView listAllTodos(@RequestParam(name = "status", defaultValue = "ALL") String status) {
        ModelAndView mav = new ModelAndView("listing");
        Map<Integer, Todo> todoMap = todoService.getToDoList();
        List<Todo> todoList = new LinkedList<>();

        // Filter todo tasks based on status
        if (!status.equals("ALL")) {
            for (Map.Entry<Integer, Todo> entry : todoMap.entrySet()) {
                Todo todo = entry.getValue();
                if (todo.getStatus().equals(status.toLowerCase())) {
                    todoList.add(todo);
                }
            }
        } else {
            todoList.addAll(todoMap.values()); // If status is ALL, include all tasks
        }

        mav.addObject("todoList", todoList);

        return mav;
    }

    // Get the /add page
    @GetMapping("/add")
    public ModelAndView getTodoForm() {
        Todo todo = new Todo();
        ModelAndView mav = new ModelAndView("add");
        mav.addObject("newTodo", todo);
        return mav;
    }

    // Post to /add page
    @PostMapping("/add")
    public String addTodo(
        @ModelAttribute("newTodo") @Valid Todo todo, 
        BindingResult result) {

        todo.setId(UUID.randomUUID().toString());
        todo.setCreatedAt(new Date());
        todo.setUpdatedAt(new Date());

        System.out.println(todo.toString());

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            
            return "add";
        }
        
        todoService.createTodo(todo);

        return "success";
    }
}
