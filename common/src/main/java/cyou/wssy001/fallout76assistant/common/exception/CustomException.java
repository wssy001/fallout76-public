package cyou.wssy001.fallout76assistant.common.exception;

import cyou.wssy001.fallout76assistant.common.enums.ErrorEnum;
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

    public CustomException(ErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
