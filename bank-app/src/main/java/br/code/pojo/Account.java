package br.code.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"branch", "number", "digit"})
public class Account {
    private String branch;
    private String number;
    private String digit;

    public String toString() {
        return branch + "-" + number + "-" + digit;
    }

}
