package br.code.resources;

import br.code.core.AccountController;
import br.code.pojo.AccountBalance;
import br.code.pojo.AccountDepositTransaction;
import br.code.pojo.AccountTransferTransaction;
import br.code.pojo.RequestStatus;
import br.code.pojo.ResponseWrapper;
import com.google.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/account")
public class AccountResource {

    private AccountController accountController;

    @Inject
    public AccountResource(AccountController accountController){
        this.accountController = accountController;
    }

    @GET
    @Path("/balances")
    @Produces(MediaType.APPLICATION_JSON)
    public Response balances() {
        ResponseWrapper<List<AccountBalance>> responseWrapper = new ResponseWrapper<>();
        try {
            List<AccountBalance> balances = accountController.balances();
            responseWrapper.setStatus(RequestStatus.SUCCESS);
            responseWrapper.setMsg("Operation Completed");
            responseWrapper.setData(balances);
        } catch (RuntimeException e) {
            responseWrapper.setStatus(RequestStatus.FAIL);
            responseWrapper.setMsg(e.getMessage());
            responseWrapper.setData(Collections.emptyList());
        }
        return Response.ok(responseWrapper).build();
    }

    @POST
    @Path("/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(AccountDepositTransaction accountDepositTransaction) {
        ResponseWrapper<AccountDepositTransaction> responseWrapper = new ResponseWrapper<>();
        try {
            accountController.deposit(accountDepositTransaction);
            responseWrapper.setStatus(RequestStatus.SUCCESS);
            responseWrapper.setMsg("Operation Completed");
            responseWrapper.setData(accountDepositTransaction);
        } catch (RuntimeException e) {
            responseWrapper.setStatus(RequestStatus.FAIL);
            responseWrapper.setMsg(e.getMessage());
            responseWrapper.setData(accountDepositTransaction);
        }
        return Response.ok(responseWrapper).build();
    }

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(AccountTransferTransaction accountTransferTransaction) {
        ResponseWrapper<AccountTransferTransaction> responseWrapper= new ResponseWrapper<>();
        try {
            accountController.transfer(accountTransferTransaction);
            responseWrapper.setStatus(RequestStatus.SUCCESS);
            responseWrapper.setMsg("Operation Completed");
            responseWrapper.setData(accountTransferTransaction);
        } catch (RuntimeException e) {
            responseWrapper.setStatus(RequestStatus.FAIL);
            responseWrapper.setMsg(e.getMessage());
            responseWrapper.setData(accountTransferTransaction);
        }
        return Response.ok(responseWrapper).build();
    }

}
