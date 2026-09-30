package praktikum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    public String name;
    public OrderInfo order;
    public boolean success;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderInfo {
        public int number;
    }
}