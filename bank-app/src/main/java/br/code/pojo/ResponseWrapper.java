package br.code.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"status", "msg", "data"})
public class ResponseWrapper<T> {
    private RequestStatus status;
    private String msg;
    private T data;
}
