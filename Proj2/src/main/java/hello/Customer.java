package hello;

import javax.persistence.*;

@Entity
@Table( name = "customers" )
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cus_id")
    private long id;
    @Column(name = "comp_id")
    private long compId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    public Customer(){

    }

    public Customer(long id, String firstName, String lastName, long compId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.compId = compId;
    }

    public Customer(String firstName, String lastName, long compId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.compId = compId;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s', compId=%d]",
                id, firstName, lastName, compId);
    }

    public void setId (long id){
        this.id = id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName (String lastName){
        this.lastName = lastName;
    }

    public void setCompId (long compId){
        this.compId = compId;
    }

    public long getId(){
        return id;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public long getCompId(){
        return compId;
    }

    // getters & setters опущены для краткости
}
