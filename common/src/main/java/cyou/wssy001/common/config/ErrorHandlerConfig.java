package cyou.wssy001.common.config;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.exception.CustomException;
import cyou.wssy001.common.vo.GlobalResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerConfig {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public GlobalResultVO<Void> handle(MethodArgumentNotValidException e) {
        List<String> list = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return GlobalResultVO.error(500, StrUtil.join(",", list));
    }

    @ExceptionHandler(value = CustomException.class)
    public GlobalResultVO<Void> handle(CustomException e) {
        return GlobalResultVO.error(e.getCode(), e.getMsg());
    }

}
