package dtmlibs.config.datasource;

public class DataHandlingException extends Exception {

    public DataHandlingException() {
        super();
    }

    public DataHandlingException(String message) {
        super(message);
    }

    public DataHandlingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataHandlingException(Throwable cause) {
        super(cause);
    }
}
