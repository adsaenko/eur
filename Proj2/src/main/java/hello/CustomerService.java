package hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.HibernateUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Service
public class CustomerService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportClient client = elasticClient();
	
	@Bean
	public TransportClient elasticClient() {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
		try {
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            //client.close();
        }
	    return client;
	}

    public void createTable() {
        System.out.println("Creating tables");
        customerRepository.save(new Customer("Jack", "Bauer", 1L));
        customerRepository.save(new Customer("Chloe", "O'Brian", 23L));
        customerRepository.save(new Customer("Kim", "Bauer", 1L));
        customerRepository.save(new Customer("David", "Palmer", 2L));
        customerRepository.save(new Customer("Michelle", "Dessler",1L));
    }

    public void clearTable(){
        System.out.println("Clearing tables");

    }

    public Customer searchElasticRow(String name){
		Customer result = new Customer();
		SearchResponse response = client.prepareSearch("postgres")
            .setTypes("customer")
            .setQuery(QueryBuilders.matchQuery("firstname", name))
			.execute()
			.actionGet();
		for (SearchHit hit : response.getHits().getHits()) {
			Long id = Long.parseLong(hit.getSource().get("id").toString());
			String firstName = hit.getSource().get("firstname").toString();
			String lastName = hit.getSource().get("lastname").toString();
			Long compId = Long.parseLong(hit.getSource().get("compId").toString());
			result.setId(id);
			result.setFirstName(firstName);
			result.setLastName(lastName);
			result.setCompId(compId);
		}
		System.out.println("result = " + result.toString());
        return result;
    }

    public void addIndElastic(List<Customer> customers){
        IndicesAdminClient indicesAdminClient  = client.admin().indices();
        CreateIndexResponse createIndexResponse = indicesAdminClient.prepareCreate("postgres").get();
        indicesAdminClient.preparePutMapping("postgres")
                .setType("customer")
                .setSource("{\"customer\": " +
                           "     {\"properties\": " +
                           "            {\"id\":{\"type\":\"long\"}, " +
                           "             \"firstname\":{\"type\":\"string\"}, " +
                           "             \"lastname\":{\"type\":\"string\"}, " +
                           "             \"compId\":{\"type\":\"long\"}" +
                           "             }" +
                           "      }" +
                           "}")
                .get();

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(customers.get(1));
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        IndexResponse response = client.prepareIndex("postgres", "customer")
                .setSource(json, XContentType.JSON)
                .get();
    }

    public List<Customer> searchRow(String firstName){
        List<Customer> result = customerRepository.findByFirstName(firstName);
        System.out.println("Querying for customer records where first_name = '" + firstName + "':");
        return result;
    }

    @Transactional
    public List<Customer> deleteRow(long cusId) {
        System.out.println("Querying for delete customer record where row number = '" + cusId + "':");
        customerRepository.deleteById(cusId);
        List<Customer> result = customerRepository.findAll();
        return result;
    }

    @Transactional
    public List<Customer> updateRow(String firstName, String lastName, long cusId, long compId) {
        System.out.println("Updating for customer records where cus_id = '" + cusId + "':");
        customerRepository.setAll(firstName,lastName,cusId,compId);
        List<Customer> result = customerRepository.findAll();
        return result;
    }

    public List<Customer> insertRow(String firstName, String lastName, long compId) {
        System.out.println("Querying for customer records where first_name = '" + firstName + "':");
        customerRepository.save(new Customer(firstName, lastName, compId));
        List<Customer> result = customerRepository.findAll();
        return result;
    }

}
