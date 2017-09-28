package hello;

import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
    List<Customer> findByFirstName(String firstName);
    void deleteById(long cusId);
    @Modifying
    @Query("update Customer c set c.firstName = ?1, c.lastName = ?2, c.compId =?3 where c.id = ?4")
    int setAll(String firstName, String lastName, long compId, long id);

    /*@Modifying
    @Query("update Customer c set c.firstName = ?1, c.lastName = ?2, c.compId =?3 where c.id = ?4")
    int deleteAll(String firstName, String lastName, long compId, long id);*/
}
