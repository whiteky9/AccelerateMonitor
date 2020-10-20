package Ford.AccelerateMonitor.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class Product {

    String name;

    protected Product(String name) {
        this.name = name;
    }

    protected Product(){ this.name = null; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to retrieve data from Jenkins Remote Access API
     * @param url Url to send GET request to
     * @return JsonNode of retrieved data
     * */
    protected JsonNode retreiveData(String url, String auth) {

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + auth);

        // create request
        HttpEntity request = new HttpEntity(headers);

        // make a request
        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);

        // get JSON response
        String json = response.getBody();

        JsonNode node = JsonNodeFactory.instance.objectNode();

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to `JsonNode`
            node = mapper.readTree(json);

        } catch (Exception e){
            e.printStackTrace();
        }

        return node;

    }


}
