package br.code.resources;

import br.code.dao.Storage;
import br.code.pojo.Account;
import br.code.pojo.AccountBalance;
import br.code.pojo.AccountDepositTransaction;
import br.code.pojo.AccountTransferTransaction;
import br.code.pojo.RequestStatus;
import br.code.pojo.ResponseWrapper;
import br.code.servlet.GuiceConfig;
import br.code.utils.GuiceJUnitRunner;
import br.code.utils.ModuleIT;
import com.google.inject.Inject;
import com.google.inject.servlet.GuiceFilter;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

@RunWith(GuiceJUnitRunner.class)
public class AccountResourceIT {

    private Storage storage;

    private static Server server;

    @Inject
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = new Server(8080);
        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addEventListener(new GuiceConfig(new ModuleIT()));
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
        server.start();
    }

    @After
    public void after() {
        storage.restore();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    @Test
    public void balanceIsEmptyCase() throws IOException {
        String url = "http://localhost:8080/account/balances";
        URLConnection connection = new URL(url).openConnection();
        String responseBody;
        try (InputStream response = connection.getInputStream();
            Scanner scanner = new Scanner(response)) {
                responseBody = scanner.nextLine();
        }
        String expectedResponseBody = "{\"status\":\"SUCCESS\",\"msg\":\"Operation Completed\",\"data\":[]}";
        assertEquals(expectedResponseBody, responseBody);
    }

    @Test
    public void simpleDepositCase() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //Deposit
        Account account = new Account("001", "1247", "0");
        BigDecimal amount = new BigDecimal(900.95);
        AccountDepositTransaction accountDepositTransaction = new AccountDepositTransaction(account, amount);

        HttpURLConnection depositConection = getConnection("http://localhost:8080/account/deposit");
        String depositPayload = mapper.writeValueAsString(accountDepositTransaction);
        sendPayload(depositConection, depositPayload);

        String parsedResponse = parseResponse(depositConection);

        //Assert
        ResponseWrapper<AccountDepositTransaction> response = new ResponseWrapper<>(
                RequestStatus.SUCCESS, "Operation Completed", accountDepositTransaction);

        String expectedResponseBody = mapper.writeValueAsString(response);
        assertEquals(expectedResponseBody, parsedResponse);
    }

    @Test
    public void simpleTransferCase() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //Deposit
        Account accountOrigin = new Account("001", "1250", "0");
        BigDecimal amount = new BigDecimal(900.95);
        AccountDepositTransaction accountDepositTransaction = new AccountDepositTransaction(accountOrigin, amount);

        HttpURLConnection depositConection = getConnection("http://localhost:8080/account/deposit");
        String depositPayload = mapper.writeValueAsString(accountDepositTransaction);
        sendPayload(depositConection, depositPayload);
        depositConection.getInputStream();

        //Transfer
        Account accountDestination = new Account("001", "1246", "0");
        amount = new BigDecimal(900);
        AccountTransferTransaction accountTransferTransaction = new AccountTransferTransaction(
                accountOrigin, accountDestination, amount);

        HttpURLConnection transferConection = getConnection("http://localhost:8080/account/transfer");
        String transferPayload = mapper.writeValueAsString(accountTransferTransaction);
        sendPayload(transferConection, transferPayload);

        String parsedResponse = parseResponse(transferConection);

        //Assert
        ResponseWrapper<AccountTransferTransaction> response = new ResponseWrapper<>(
                RequestStatus.SUCCESS, "Operation Completed", accountTransferTransaction);

        String expectedResponseBody = mapper.writeValueAsString(response);
        assertEquals(expectedResponseBody, parsedResponse);
    }

    @Test
    public void simpleTransferCaseWithAccountNotFound() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //Transfer
        Account accountOrigin = new Account("001", "1250", "0");
        Account accountDestination = new Account("001", "1246", "0");
        BigDecimal amount = new BigDecimal(900);
        AccountTransferTransaction accountTransferTransaction = new AccountTransferTransaction(
                accountOrigin, accountDestination, amount);

        HttpURLConnection transferConection = getConnection("http://localhost:8080/account/transfer");
        String transferPayload = mapper.writeValueAsString(accountTransferTransaction);
        sendPayload(transferConection, transferPayload);

        String parsedResponse = parseResponse(transferConection);

        //Assert
        ResponseWrapper<AccountTransferTransaction> response = new ResponseWrapper<>(
                RequestStatus.FAIL, "Account not found.", accountTransferTransaction);

        String expectedResponseBody = mapper.writeValueAsString(response);
        assertEquals(expectedResponseBody, parsedResponse);
    }

    @Test
    public void simpleTransferCaseWithNotEnoughFunds() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //Deposit
        Account accountOrigin = new Account("001", "1250", "0");
        BigDecimal amount = new BigDecimal(90.95);
        AccountDepositTransaction accountDepositTransaction = new AccountDepositTransaction(accountOrigin, amount);

        HttpURLConnection depositConection = getConnection("http://localhost:8080/account/deposit");
        String depositPayload = mapper.writeValueAsString(accountDepositTransaction);
        sendPayload(depositConection, depositPayload);
        depositConection.getInputStream();

        //Transfer
        Account accountDestination = new Account("001", "1246", "0");
        amount = new BigDecimal(900);
        AccountTransferTransaction accountTransferTransaction = new AccountTransferTransaction(
                accountOrigin, accountDestination, amount);

        HttpURLConnection transferConection = getConnection("http://localhost:8080/account/transfer");
        String transferPayload = mapper.writeValueAsString(accountTransferTransaction);
        sendPayload(transferConection, transferPayload);

        String parsedResponse = parseResponse(transferConection);

        //Assert
        ResponseWrapper<AccountTransferTransaction> response = new ResponseWrapper<>(
                RequestStatus.FAIL, "Not enough funds.", accountTransferTransaction);

        String expectedResponseBody = mapper.writeValueAsString(response);
        assertEquals(expectedResponseBody, parsedResponse);
    }

    @Test
    public void simpleDepositCaseAndCheckBalance() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //Deposit
        Account account = new Account("001", "1247", "0");
        BigDecimal amount = new BigDecimal(900.95).setScale(2, RoundingMode.HALF_UP);
        AccountDepositTransaction accountDepositTransaction = new AccountDepositTransaction(account, amount);

        HttpURLConnection depositConection = getConnection("http://localhost:8080/account/deposit");
        String depositPayload = mapper.writeValueAsString(accountDepositTransaction);
        sendPayload(depositConection, depositPayload);
        depositConection.getInputStream();

        //Balances
        String responseBody;
        String urlBalance = "http://localhost:8080/account/balances";
        URLConnection connectionBalance = new URL(urlBalance).openConnection();
        try (InputStream response = connectionBalance.getInputStream(); Scanner scanner = new Scanner(response)) {
            responseBody = scanner.nextLine();
        }

        //Assert
        AccountBalance accountBalance = new AccountBalance(account, amount);
        ResponseWrapper<List<AccountBalance>> response = new ResponseWrapper<>(
                RequestStatus.SUCCESS, "Operation Completed", Arrays.asList(accountBalance));
        String expectedResponseBody = mapper.writeValueAsString(response);
        assertEquals(expectedResponseBody, responseBody);
    }

    @Test
    public void simpleTransferCaseAndCheckBalance() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //Deposit
        Account accountOrigin = new Account("001", "1250", "0");
        BigDecimal amount = new BigDecimal(900.95).setScale(2, BigDecimal.ROUND_HALF_UP);
        AccountDepositTransaction accountDepositTransaction = new AccountDepositTransaction(accountOrigin, amount);

        HttpURLConnection depositConection = getConnection("http://localhost:8080/account/deposit");
        String depositPayload = mapper.writeValueAsString(accountDepositTransaction);
        sendPayload(depositConection, depositPayload);
        depositConection.getInputStream();

        //Transfer
        Account accountDestination = new Account("001", "1246", "0");
        amount = new BigDecimal(900).setScale(2, BigDecimal.ROUND_HALF_UP);
        AccountTransferTransaction accountTransferTransaction = new AccountTransferTransaction(
                accountOrigin, accountDestination, amount);

        HttpURLConnection transferConection = getConnection("http://localhost:8080/account/transfer");
        String transferPayload = mapper.writeValueAsString(accountTransferTransaction);
        sendPayload(transferConection, transferPayload);
        transferConection.getInputStream();

        String urlBalance = "http://localhost:8080/account/balances";
        URLConnection connectionBalance = new URL(urlBalance).openConnection();
        try (InputStream response = connectionBalance.getInputStream();
             Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.nextLine();
            String expectedResponseBody = "{\"status\":\"SUCCESS\",\"msg\":\"Operation Completed\",\"data\":[{\"account\":{\"branch\":\"001\",\"number\":\"1246\",\"digit\":\"0\"},\"balance\":900.00},{\"account\":{\"branch\":\"001\",\"number\":\"1250\",\"digit\":\"0\"},\"balance\":0.95}]}";
            assertEquals(expectedResponseBody, responseBody);
        }
    }

    private HttpURLConnection getConnection(String spec) throws IOException {
        URL urlDeposit = new URL (spec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlDeposit.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoOutput(true);
        return httpURLConnection;
    }

    private void sendPayload(HttpURLConnection httpURLConnection, String payload) throws IOException {
        try(OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    private String parseResponse(HttpURLConnection httpURLConnection) throws IOException {
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

}
