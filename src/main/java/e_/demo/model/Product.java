package e_.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // generates a no-arg constructor
@AllArgsConstructor // generates an all-args constructor
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private Double price;
}
