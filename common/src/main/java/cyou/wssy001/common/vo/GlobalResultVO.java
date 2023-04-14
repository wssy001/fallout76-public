package cyou.wssy001.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@AllArgsConstructor
public class GlobalResultVO<T> {
    private LocalDateTime time;
    private int code;
    private String msg;
    private T data;

    public static <T> GlobalResultVO<T> ok() {
        return ok(null);
    }

    public static <T> GlobalResultVO<T> ok(T data) {
        return ok(200, null);
    }

    public static <T> GlobalResultVO<T> ok(int code, String msg) {
        return ok(code, msg, null);
    }

    public static <T> GlobalResultVO<T> ok(int code, String msg, T data) {
        return new GlobalResultVO<>(LocalDateTime.now(ZoneId.of("GMT+8")), code, msg, data);
    }

    public static <T> GlobalResultVO<T> error() {
        return error(null);
    }

    public static <T> GlobalResultVO<T> error(T data) {
        return error(-200, null);
    }

    public static <T> GlobalResultVO<T> error(int code, String msg) {
        return error(code, msg, null);
    }

    public static <T> GlobalResultVO<T> error(int code, String msg, T data) {
        return new GlobalResultVO<>(LocalDateTime.now(ZoneId.of("GMT+8")), code, msg, data);
    }
}
