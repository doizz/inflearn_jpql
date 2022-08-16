package jpql;

import javax.persistence.*;

@Entity
@Table(name="ORDERS")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private int orderAmount;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

/**
 * - JPQL 문법
 *  - select m from Member as m where m.age>18
 *  - 엔티티와 속성은 대소문자 구분 O
 *  - JPQL키워드는 대소문자 구분 X
 *  - 엔티티 이름 사용, 테이블 이름이 아님
 *  - 별칭은 필수
 *
 * - 결과조회 API
 *  - query.getResultList() : 결과가 하나 이상일 때, 리스트 반환 ( 결과가 없으면 빈 리스트 반환)
 *  - query.getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
 *   - 결과가 없으면 : javax.persistence.NoResultException
 *   - 결과가 둘 이상이면 : javax.persistence.NonUniqueResultException
 *
 *
 */