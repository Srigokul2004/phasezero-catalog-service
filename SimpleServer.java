import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleServer {
    private static Map<String, Product> products = new ConcurrentHashMap<>();
    private static int idCounter = 1;
    
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/products", new ProductHandler());
        server.setExecutor(null);
        server.start();
        
        System.out.println("Server started on http://localhost:8080");
        System.out.println("Try: http://localhost:8080/products");
    }
    
    static class ProductHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            
            String response = "";
            int statusCode = 200;
            
            try {
                if ("GET".equals(method) && "/products".equals(path)) {
                    if (query != null && query.startsWith("name=")) {
                        String name = query.substring(5);
                        response = searchProducts(name);
                    } else {
                        response = getAllProducts();
                    }
                } else if ("GET".equals(method) && path.startsWith("/products/category/")) {
                    String category = path.substring(19);
                    response = filterByCategory(category);
                } else if ("GET".equals(method) && "/products/sorted".equals(path)) {
                    response = getSortedProducts();
                } else if ("GET".equals(method) && "/products/inventory/value".equals(path)) {
                    response = getInventoryValue();
                } else if ("POST".equals(method) && "/products".equals(path)) {
                    String body = readBody(exchange);
                    response = createProduct(body);
                    statusCode = 201;
                } else {
                    response = "{\"message\":\"Endpoint not found\"}";
                    statusCode = 404;
                }
            } catch (Exception e) {
                response = "{\"message\":\"" + e.getMessage() + "\"}";
                statusCode = 400;
            }
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        private String readBody(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            return new Scanner(is).useDelimiter("\\A").next();
        }
        
        private String getAllProducts() {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Product p : products.values()) {
                if (!first) sb.append(",");
                sb.append(p.toJson());
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        
        private String searchProducts(String name) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Product p : products.values()) {
                if (p.partName.toLowerCase().contains(name.toLowerCase())) {
                    if (!first) sb.append(",");
                    sb.append(p.toJson());
                    first = false;
                }
            }
            sb.append("]");
            return sb.toString();
        }
        
        private String createProduct(String json) {
            // Simple JSON parsing
            String partNumber = extractValue(json, "partNumber");
            String partName = extractValue(json, "partName").toLowerCase();
            String category = extractValue(json, "category");
            double price = Double.parseDouble(extractValue(json, "price"));
            int stock = Integer.parseInt(extractValue(json, "stock"));
            
            if (products.containsKey(partNumber)) {
                throw new RuntimeException("Product already exists");
            }
            
            Product product = new Product(idCounter++, partNumber, partName, category, price, stock);
            products.put(partNumber, product);
            return product.toJson();
        }
        
        private String filterByCategory(String category) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Product p : products.values()) {
                if (p.category.equalsIgnoreCase(category)) {
                    if (!first) sb.append(",");
                    sb.append(p.toJson());
                    first = false;
                }
            }
            sb.append("]");
            return sb.toString();
        }
        
        private String getSortedProducts() {
            List<Product> sorted = new ArrayList<>(products.values());
            sorted.sort((a, b) -> Double.compare(a.price, b.price));
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < sorted.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(sorted.get(i).toJson());
            }
            sb.append("]");
            return sb.toString();
        }
        
        private String getInventoryValue() {
            double total = 0;
            for (Product p : products.values()) {
                total += p.price * p.stock;
            }
            return "{\"totalValue\":" + total + "}";
        }
        
        private String extractValue(String json, String key) {
            String pattern = "\"" + key + "\"\\s*:\\s*([^,}]+)";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                String value = m.group(1).trim();
                return value.replaceAll("^\"|\"$", ""); // Remove quotes
            }
            return "";
        }
    }
    
    static class Product {
        int id;
        String partNumber, partName, category;
        double price;
        int stock;
        
        Product(int id, String partNumber, String partName, String category, double price, int stock) {
            this.id = id;
            this.partNumber = partNumber;
            this.partName = partName;
            this.category = category;
            this.price = price;
            this.stock = stock;
        }
        
        String toJson() {
            return String.format("{\"id\":%d,\"partNumber\":\"%s\",\"partName\":\"%s\",\"category\":\"%s\",\"price\":%.2f,\"stock\":%d}", 
                id, partNumber, partName, category, price, stock);
        }
    }
}