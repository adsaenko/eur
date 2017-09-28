package hello;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;


@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(path = "/elasticput", method = RequestMethod.GET)
    public String elasticPut(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        List<Customer> customers = customerService.searchRow(name);
        customerService.addIndElastic(customers);
        String result = "";
        result = customers.toString();
        return result;
    }

    @RequestMapping(path = "/elasticget", method = RequestMethod.GET)
    public String elasticGet(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        Customer customers = customerService.searchElasticRow(name);
        String result = "";
        result = customers.toString();
        return result;
    }

    @RequestMapping(path = "/greeting", method = RequestMethod.GET)
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        customerService.createTable();
        List<Customer> customers = customerService.searchRow(name);
        String result = "";
        result = customers.toString();
        return result;
    }

    @RequestMapping(path = "/greeting", method = RequestMethod.DELETE)
    public String delete(@RequestBody Customer jsonObj) {
        List<Customer> customers = customerService.deleteRow(jsonObj.getId());
        String result = "";
        result = customers.toString();
        return result;
    }

    @RequestMapping(path = "/greeting", method = RequestMethod.PUT)
    public String update(@RequestBody Customer jsonObj) {
        List<Customer> customers;
        customers = customerService.updateRow(jsonObj.getFirstName(),jsonObj.getLastName(),jsonObj.getCompId(),jsonObj.getId());
        String result = "";
        result = customers.toString();
        return result;
    }

    @RequestMapping(path = "/greeting", method = RequestMethod.POST)
    public String insert(@RequestBody Customer jsonObj) {
        List<Customer> customers;
        customers = customerService.insertRow(jsonObj.getFirstName(),jsonObj.getLastName(),jsonObj.getCompId());
        String result = "";
        result = customers.toString();
        return result;
    }


}
