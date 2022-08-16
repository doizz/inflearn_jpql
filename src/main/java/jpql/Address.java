package jpql;


import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;
    private String zipcode;
    private String street;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
