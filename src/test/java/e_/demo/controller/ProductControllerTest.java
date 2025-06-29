
package e_.demo.controller;

import e_.demo.model.Product;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void test_Get_All_Products(){

        String url= "/api/products?page=1&size=4&sort=price,asc";

        ResponseEntity<Product[]> response=restTemplate.getForEntity(url,Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(4);
        assertThat(Arrays.stream(response.getBody()).findFirst().get().getPrice()).isEqualTo(250);

    }

    @Test
    void test_Get_Product_By_Id(){
        String url="/api/products/685fb0fb523a9e812bd4a949";

        ResponseEntity<Product> response=restTemplate.getForEntity(url,Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("685fb0fb523a9e812bd4a949");

    }

    @Test
    void test_Post_Product() {
        String url = "/api/products";
        Product product = new Product(null, "Apple", "So sweet", "Fruit", 250.00);

        HttpEntity<Product> request = getProductHttpEntity(product);

        ResponseEntity<Product> response = restTemplate.postForEntity(url, request, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Apple");
    }

    @Test
    void test_Put_Product_By_Id(){
        String id="685fb0fb523a9e812bd4a949";
        String url="/api/products/"+id;
        Product product=new Product(id,"Iphone","A high-performance laptop","Electronics",25000.00);

        HttpEntity<Product> request = getProductHttpEntity(product);

        ResponseEntity<Product> response=restTemplate.exchange(url,HttpMethod.PUT,request,Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPrice()).isEqualTo(25000.00);
    }


    @Test
    void test_Delete_Product_By_Id(){
        String id="685fb887a5b443fe22193c21";
        String url="/api/products/"+id;

        ResponseEntity<Product> response=restTemplate.exchange(url,HttpMethod.DELETE,HttpEntity.EMPTY,Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void test_Serch_Product_By_Name(){
        String url="/api/products/search?name=ThinkPad";
        ResponseEntity<Product[]> response=restTemplate.getForEntity(url,Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(2);

    }

    @Test
    void test_Get_Product_By_Range(){
        String url="/api/products/price-range?minPrice=1000&maxPrice=20000";
        ResponseEntity<Product[]> response=restTemplate.getForEntity(url,Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(3);
    }

    @Test
    void test_Get_Product_By_Category(){
        String url="/api/products/category/Fruit";
        ResponseEntity<Product[]> response=restTemplate.getForEntity(url,Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(response.getBody()).findFirst().get().getName()).isEqualTo("Apple");
    }





    private static HttpEntity<Product> getProductHttpEntity(Product product) {
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Product> request=new HttpEntity<>(product,headers);
        return request;
    }






}