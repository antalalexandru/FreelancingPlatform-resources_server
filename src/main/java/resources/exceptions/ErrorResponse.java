package resources.exceptions;

public class ErrorResponse {
    private String message;
    private String errorCode;
    private String type;

    public ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public ErrorResponse(String message, String errorCode, Class<?> type) {
        this.message = message;
        this.errorCode = errorCode;
        this.type = type.toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
