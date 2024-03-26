package ibf2023.ssf.practice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Login {

    @NotEmpty(message="Name cannot be null")
    @Size(min=5, max=32, message="Name must be between 5 to 32 characters")
    private String fullName;

    @NotNull(message = "Age cannot be null")
    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;

    public Login() {};
    
    public Login(String fullName, Integer age) {
        this.fullName = fullName;
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Login [fullName=" + fullName + ", age=" + age + "]";
    }
}
