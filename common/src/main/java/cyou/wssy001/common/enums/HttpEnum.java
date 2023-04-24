package cyou.wssy001.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: HTTP连接时的一些常量
 * @Author: Tyler
 * @Date: 2023/3/15 15:11
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum HttpEnum {
    USER_AGENT("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36"),
    NUKA_CODE_WEB_URL("https://nukacrypt.com/graphql/graphql"),
    NUKA_CODE_WEB_QUERY_BODY("{\"operationName\":null,\"variables\":{},\"query\":\"{\\n  nukeCodes {\\n    alpha\\n    bravo\\n    charlie\\n    __typename\\n  }\\n}\"}"),
    PHOTOS_WEB_URL(""),
    ;

    private final String value;
}
