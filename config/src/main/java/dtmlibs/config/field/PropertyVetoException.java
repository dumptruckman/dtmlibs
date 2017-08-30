package dtmlibs.config.field;

public class PropertyVetoException extends Exception {

    public PropertyVetoException() {
        super();
    }

    public PropertyVetoException(String message) {
        super(message);
    }

    public PropertyVetoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyVetoException(Throwable cause) {
        super(cause);
    }
}
