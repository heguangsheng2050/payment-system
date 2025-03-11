import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;

public class SimplePaymentGateway {
    private static final String LOG_FILE = "/workspace/payment-system.log";
    
    // Logger class to handle JSON logging
    static class Logger {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        
        public static synchronized void log(String level, String message, Map<String, Object> data) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                StringBuilder sb = new StringBuilder();
                sb.append(dateFormat.format(new Date()));
                sb.append(" - ");
                sb.append(level);
                sb.append(" - ");
                
                // Add message
                sb.append(message);
                
                // Add JSON data if provided
                if (data != null && !data.isEmpty()) {
                    sb.append(" - ");
                    sb.append(mapToJson(data));
                }
                
                writer.println(sb.toString());
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        }
        
        public static void info(String message) {
            log("INFO", message, null);
        }
        
        public static void info(String message, Map<String, Object> data) {
            log("INFO", message, data);
        }
        
        public static void error(String message) {
            log("ERROR", message, null);
        }
        
        public static void error(String message, Map<String, Object> data) {
            log("ERROR", message, data);
        }
        
        private static String mapToJson(Map<String, Object> map) {
            StringBuilder json = new StringBuilder("{");
            boolean first = true;
            
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!first) {
                    json.append(",");
                }
                first = false;
                
                json.append("\"").append(entry.getKey()).append("\":");
                Object value = entry.getValue();
                
                if (value == null) {
                    json.append("null");
                } else if (value instanceof Number) {
                    json.append(value);
                } else if (value instanceof Boolean) {
                    json.append(value);
                } else if (value instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> nestedMap = (Map<String, Object>) value;
                    json.append(mapToJson(nestedMap));
                } else {
                    // Escape special characters in strings
                    String stringValue = value.toString()
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
                    json.append("\"").append(stringValue).append("\"");
                }
            }
            
            json.append("}");
            return json.toString();
        }
    }
    
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8083), 0);
        server.setExecutor(Executors.newCachedThreadPool());
        
        // Create context for health check
        HttpContext healthContext = server.createContext("/paybridge/api/v1/gateway/health", new HealthCheckHandler());
        
        // Create context for payment processing with authentication
        HttpContext paymentContext = server.createContext("/paybridge/api/v1/gateway/process", new PaymentHandler());
        paymentContext.setAuthenticator(new SimpleAuthenticator("paybridge", "BridgePass123!"));
        
        Logger.info("Starting SimplePaymentGateway on port 8083...");
        server.start();
        Logger.info("SimplePaymentGateway is running!");
    }
    
    static class HealthCheckHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "PayBridge service is healthy";
            
            // Log the request
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("method", exchange.getRequestMethod());
            requestData.put("path", exchange.getRequestURI().getPath());
            requestData.put("remoteAddress", exchange.getRemoteAddress().toString());
            Logger.info("Health check request received", requestData);
            
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
            
            // Log the response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 200);
            responseData.put("body", response);
            Logger.info("Health check response sent", responseData);
        }
    }
    
    static class PaymentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                // Log method not allowed error
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("method", exchange.getRequestMethod());
                errorData.put("path", exchange.getRequestURI().getPath());
                errorData.put("error", "Method not allowed");
                errorData.put("status", 405);
                Logger.error("Invalid request method", errorData);
                
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                return;
            }
            
            // Read request body
            String requestBody = readInputStream(exchange.getRequestBody());
            
            // Log the request
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("method", exchange.getRequestMethod());
            requestData.put("path", exchange.getRequestURI().getPath());
            requestData.put("remoteAddress", exchange.getRemoteAddress().toString());
            requestData.put("body", requestBody);
            Logger.info("Payment request received", requestData);
            
            // Extract account number from request
            String accountNumber = extractAccountNumber(requestBody);
            boolean isSuccess = isEvenAccountNumber(accountNumber);
            
            // Prepare response
            String responseJson = createResponse(isSuccess);
            
            // Send response
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseJson.getBytes(StandardCharsets.UTF_8));
            }
            
            // Log the response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 200);
            responseData.put("body", responseJson);
            responseData.put("accountNumber", accountNumber);
            responseData.put("isSuccess", isSuccess);
            Logger.info("Payment response sent", responseData);
        }
        
        private String readInputStream(InputStream is) {
            try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        }
        
        private String extractAccountNumber(String json) {
            // Simple regex to extract account number from JSON
            Pattern pattern = Pattern.compile("\"accountNumber\"\\s*:\\s*\"([^\"]+)\"");
            java.util.regex.Matcher matcher = pattern.matcher(json);
            return matcher.find() ? matcher.group(1) : "";
        }
        
        private boolean isEvenAccountNumber(String accountNumber) {
            // Remove any non-numeric characters
            String numericOnly = accountNumber.replaceAll("[^0-9]", "");
            if (numericOnly.isEmpty()) {
                return false;
            }
            // Get the last digit
            int lastDigit = Character.getNumericValue(numericOnly.charAt(numericOnly.length() - 1));
            return lastDigit % 2 == 0;
        }
        
        private String createResponse(boolean isSuccess) {
            String status = isSuccess ? "Success" : "Failure";
            String message = isSuccess ? "Transaction is successful" : "Transaction failed";
            String transactionId = UUID.randomUUID().toString();
            String processorResponse = isSuccess ? "Approved" : "Declined";
            String processorResponseCode = isSuccess ? "00" : "05";
            long timestamp = System.currentTimeMillis();
            
            return String.format(
                "{\"status\":\"%s\",\"message\":\"%s\",\"transactionId\":\"%s\",\"processorResponse\":\"%s\",\"processorResponseCode\":\"%s\",\"timestamp\":%d}",
                status, message, transactionId, processorResponse, processorResponseCode, timestamp
            );
        }
    }
    
    static class SimpleAuthenticator extends BasicAuthenticator {
        private final String username;
        private final String password;
        
        public SimpleAuthenticator(String username, String password) {
            super("paybridge");
            this.username = username;
            this.password = password;
        }
        
        @Override
        public boolean checkCredentials(String username, String password) {
            boolean isValid = this.username.equals(username) && this.password.equals(password);
            
            // Log authentication attempt
            Map<String, Object> authData = new HashMap<>();
            authData.put("username", username);
            authData.put("success", isValid);
            
            if (isValid) {
                Logger.info("Authentication successful", authData);
            } else {
                Logger.error("Authentication failed", authData);
            }
            
            return isValid;
        }
    }
}