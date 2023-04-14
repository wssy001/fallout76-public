package cyou.wssy001.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private int code;
    private String msg;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public CustomException(int code) {
        super(String.valueOf(code));
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
