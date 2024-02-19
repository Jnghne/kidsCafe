package project.kidscafe.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ApiRequest<T> {
    private T classType;
    private Object params;

    public static <T> ApiRequest<T> of(Object params, T classType) {
        return new ApiRequest<>(classType,params);
    }
}
