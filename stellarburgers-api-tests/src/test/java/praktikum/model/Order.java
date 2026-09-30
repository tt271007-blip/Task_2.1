package praktikum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    public List<String> ingredients;
    public String _id;
    public String status;
    public int number;
    public String createdAt;
    public String updatedAt;
}