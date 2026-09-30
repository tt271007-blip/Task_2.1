package praktikum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersResponse {
    public boolean success;
    public List<Order> orders;
    public int total;
    public int totalToday;
}