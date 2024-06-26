package ibf2023.ssf.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

		// To load data from todos.txt to Redis ----------------------
		todoService.saveExistingTodoList(Util.KEY_TODO);
		todoService.getToDoList();
	}
}
