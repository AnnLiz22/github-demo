package lietz.anna.demo.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message, Exception e) {
        super(message);
    }
}
