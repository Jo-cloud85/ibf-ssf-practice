package ibf2023.ssf.practice.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ibf2023.ssf.practice.model.Todo;

public class HelperRepo {

    private final String filename = "todos.txt";

    private String writeDataFromTxtToString() {
        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            System.out.println("Unable to locate file: " + filename);
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();
                
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to read file");
            }
        }
        return null;
    }

    private List<String> parseStringToList() {
        String todoString = writeDataFromTxtToString();
        // System.out.println(todoString);
        String todoStringTrimmed = todoString.substring(3, todoString.length()-4);
        todoStringTrimmed = replaceNonStandardDoubleQuotes(todoStringTrimmed);
        String[] todoArr = todoStringTrimmed.split("\\},\n\\{");
        List<String> todoStrList = new LinkedList<>();
        for (String t : todoArr) {
            todoStrList.add(t);
        }
        // todoStrList.forEach(System.out::println);

        return todoStrList;
    }

    public List<Todo> convertTodoStrListToTodoList() throws ParseException {
        List<String> todoStrList = parseStringToList();
        List<Todo> todoList = new LinkedList<>();
        
        for (String todoStr : todoStrList) {
            // System.out.println(todoStr);
            Todo todo = new Todo();
            
            String[] todoStrArr = todoStr.split(",\n");

            todo.setId(extractValue(todoStrArr[0]));
            todo.setName(extractValue(todoStrArr[1]));
            todo.setDescription(extractValue(todoStrArr[2]));

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy");
            
            String dueDateStr = extractValue(todoStrArr[3]);
            Date dueDate = sdf.parse(dueDateStr);
            // System.out.println(dueDate);
            todo.setDueDate(dueDate);

            todo.setPriority(extractValue(todoStrArr[4]));
            todo.setStatus(extractValue(todoStrArr[5]));

            String createdAtStr = extractValue(todoStrArr[6]);
            Date createdAtDate = sdf.parse(createdAtStr);
            todo.setCreatedAt(createdAtDate);

            String updatedAtStr = extractValue(todoStrArr[7]).trim();
            Date updatedAtDate = sdf.parse(updatedAtStr);
            todo.setUpdatedAt(updatedAtDate);

            todoList.add(todo);
        }
        // System.out.println(todoList);
        return todoList;
    }

    // Helper method to extract value
    private String extractValue(String valueString) {
        int startIndex = findThirdDoubleQuoteIndex(valueString) + 1;
        int endIndex = valueString.lastIndexOf("\"");

        // Extract the substring between the double quotes
        String value = valueString.substring(startIndex, endIndex);

        return value;
    }

    // Helper method
    private int findThirdDoubleQuoteIndex(String inputString) {
        int count = 0;
        for (int i = 0; i < inputString.length(); i++) {
            if (inputString.charAt(i) == '"') {
                count++;
                if (count == 3) {
                    return i;
                }
            }
        }
        return -1; // Return -1 if the third double quote is not found
    }

    // Helper method
    private String replaceNonStandardDoubleQuotes(String jsonString) {
        // Replace non-standard double quotes with standard ASCII double quotes
        jsonString = jsonString.replace('\u201c', '"'); // Left double quotation mark
        jsonString = jsonString.replace('\u201d', '"'); // Right double quotation mark        
        return jsonString;
    }
}
