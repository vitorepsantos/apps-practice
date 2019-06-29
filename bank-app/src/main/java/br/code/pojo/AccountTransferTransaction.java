package br.code.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"accountOrigin", "accountDestination", "amount"})
public class AccountTransferTransaction {
    private Account accountOrigin;
    private Account accountDestination;
    private BigDecimal amount;
}
