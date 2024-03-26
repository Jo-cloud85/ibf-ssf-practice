package ibf2023.ssf.practice.controller;

import java.io.IOException;
import java.text.ParseException;
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
        HttpSession session) throws ParseException {

        if (session.getAttribute("login") != null) {
            ModelAndView mav = new ModelAndView("listing");
            List<Todo> todoList = todoService.getToDoList();
            List<Todo> filteredTodoList = new LinkedList<>();

            // Filter todo tasks based on status
            if (!status.equals("ALL")) {
                for (Todo todo : todoList) {
                    // actually I have alrdy lowercased in HTML
                    if (todo.getStatus().equals(status.toLowerCase())) { 
                        filteredTodoList.add(todo);
                    }
                }
            } else {
                filteredTodoList.addAll(todoList); // If status is ALL, include all tasks
            }

            mav.addObject("showTodoList", true); // Show the todo list since user is logged in
            mav.addObject("todoList", filteredTodoList);
            return mav;
        } else {
            //mav.addObject("showTodoList", false); // Don't show the todo list if user is not logged in
            ModelAndView mav = new ModelAndView("refused");
            return mav;
        }
    }

    // Get /add page
    @GetMapping("/todo/add")
    public ModelAndView getTodoForm(
        HttpSession session) {

        Todo todo = new Todo();
        
        if (session.getAttribute("login") != null) {
            ModelAndView mav = new ModelAndView("add");
            mav.addObject("newTodo", todo);
            return mav;
        } else {
            ModelAndView mav = new ModelAndView("refused");
            return mav;
        }
    }

    // Post to /add page
    @PostMapping("/todo/add")
    public String addTodo(
        HttpSession session,
        @ModelAttribute("newTodo") @Valid Todo todo, 
        BindingResult result) throws IOException {

        if (session.getAttribute("login") != null) {
            todo.setId(UUID.randomUUID().toString());
            todo.setCreatedAt(new Date());
            todo.setUpdatedAt(new Date());
    
            if (result.hasErrors()) {
                System.out.println(result.getAllErrors());
                return "add";
            }
            
            todoService.createTodo(todo);
    
            return "success";
        } else {
            return "refused";
        }
        
    }

    @GetMapping("/todo/update/{id}")
    public ModelAndView updateTodo(
        HttpSession session,
        @PathVariable("id") String id) throws IOException {
        
        if (session.getAttribute("login") != null) {
            Todo todo = todoService.getTodoById(id);
            ModelAndView mav = new ModelAndView("update");
            mav.addObject("updateTodo", todo);
            return mav;
        } else {
            ModelAndView mav = new ModelAndView("refused");
            return mav;
        }
    }

    @PostMapping("/todo/update/{id}")
    public String postUpdateTodo(
        HttpSession session,
        @PathVariable("id") String id,
        @ModelAttribute("updateTodo") @Valid Todo todo, 
        BindingResult result) throws IOException {

        if (session.getAttribute("login") != null) {
            todo.setId(id);
            todo.setCreatedAt(todo.getCreatedAt());
            todo.setUpdatedAt(new Date());
    
            if (result.hasErrors()) {
                System.out.println(result.getAllErrors());
                return "update";
            }
            
            todoService.updateTodo(todo);

            return "redirect:/todo";
        } else {
            return "refused";
        }
    }

    @GetMapping("/todo/delete/{id}")
    public String deleteTodo(@PathVariable("id") String id) throws IOException {
        Todo todo = todoService.getTodoById(id);
        System.out.println("Deleting...." + todo);
        todoService.deleteTodo(todo);
        return "redirect:/todo";
    }
}
