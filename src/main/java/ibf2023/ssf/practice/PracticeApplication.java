package ibf2023.ssf.practice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ibf2023.ssf.practice.model.Todo;
import ibf2023.ssf.practice.repository.HelperRepo;
import ibf2023.ssf.practice.service.TodoService;
import ibf2023.ssf.practice.util.Util;

@SpringBootApplication
public class PracticeApplication implements CommandLineRunner {

	@Autowired
	TodoService todoService;

	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		HelperRepo helperRepo = new HelperRepo();
        List<Todo> todoList = helperRepo.convertTodoStrListToTodoList();
		// System.out.println(todoList);

		// .saveExisitingTodoList() method will convert each Todo object to a String
		//todoService.saveExistingTodoList(Util.KEY_TODO, todoList);
		// todoService.getToDoList();
	}
}
