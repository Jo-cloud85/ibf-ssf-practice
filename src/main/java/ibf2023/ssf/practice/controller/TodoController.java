package ibf2023.ssf.practice.controller;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.service.TodoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping
public class TodoController {
    
    @Autowired
    private TodoService todoService;

    // Get listing page
    @GetMapping("/todo")
    public ModelAndView listAllTodos(
        @RequestParam(name = "status", defaultValue = "ALL") String status,
        HttpSession session) {

        ModelAndView mav = new ModelAndView("listing");

        if (session.getAttribute("fullName") != null && session.getAttribute("age") != null) {
            Map<String, Todo> todoMap = todoService.getToDoList();
            List<Todo> todoList = new LinkedList<>();

            // Filter todo tasks based on status
            if (!status.equals("ALL")) {
                for (Map.Entry<String, Todo> entry : todoMap.entrySet()) {
                    Todo todo = entry.getValue();
                    if (todo.getStatus().equals(status.toLowerCase())) {
                        todoList.add(todo);
                    }
                }
            } else {
                todoList.addAll(todoMap.values()); // If status is ALL, include all tasks
            }

            mav.addObject("showTodoList", true); // Show the todo list since user is logged in
            mav.addObject("todoList", todoList);
        } else {
            mav.addObject("showTodoList", false); // Don't show the todo list if user is not logged in
        }

        return mav;
    }

    // Get /add page
    @GetMapping("/todo/add")
    public ModelAndView getTodoForm() {
        Todo todo = new Todo();
        ModelAndView mav = new ModelAndView("add");
        mav.addObject("newTodo", todo);
        return mav;
    }

    // Post to /add page
    @PostMapping("/todo/add")
    public String addTodo(
        @ModelAttribute("newTodo") @Valid Todo todo, 
        BindingResult result) {

        todo.setId(UUID.randomUUID().toString());
        todo.setCreatedAt(new Date());
        todo.setUpdatedAt(new Date());

        // System.out.println(todo.toString());

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "add";
        }
        
        todoService.createTodo(todo);

        return "success";
    }

    @GetMapping("/todo/update/{id}")
    public ModelAndView updateTodo(@PathVariable("id") String id) throws IOException {
        Todo todo = todoService.getTodoById(id);
        ModelAndView mav = new ModelAndView("update");
        mav.addObject("updateTodo", todo);
        //todoService.updateTodo(todo);
        return mav;
    }

    @PostMapping("/todo/update/{id}")
    public String postUpdateTodo(
        @PathVariable("id") String id,
        @ModelAttribute("updateTodo") @Valid Todo todo, 
        BindingResult result) throws IOException {

        todo.setId(id);
        todo.setCreatedAt(todo.getCreatedAt());
        todo.setUpdatedAt(new Date());

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "update";
        }
        
        todoService.updateTodo(todo);
        return "redirect:/todo";
    }

    @GetMapping("/todo/delete/{id}")
    public String deleteTodo(@PathVariable("id") String id) throws IOException {
        Todo todo = todoService.getTodoById(id);
        System.out.println("Deleting...." + todo);
        todoService.deleteTodo(todo);
        return "redirect:/todo";
    }
}
