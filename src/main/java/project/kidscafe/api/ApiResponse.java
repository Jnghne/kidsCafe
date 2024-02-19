package project.kidscafe.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final HttpStatus status;
    private final int code;
    private final String message;
    private final T data;

    // 생성자
    private ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.code = status.value();
        this.message = message;
        this.data = data;
    }
    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
    // ------------------
    // 응답 상태가 200일 때 사용
    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }
    public static ApiResponse<Void> ok() {
        return of(HttpStatus.OK, HttpStatus.OK.name(), null);
    }

    // -----------------
    // 예외 발생 시 사용
    public static ApiResponse<String> of(HttpStatus status, String message) {
        return of(status, message, null);
    }

}
