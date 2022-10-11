package Capstone_team1.Jubging.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseTimeEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "cnt")
    private Integer cnt;

    @Column(name="contents", length = 800)
    private String contents;

    @Column(name="img_url", length = 60)
    private String imgUrl;

    public Product update(Integer price, String name, Integer cnt, String contents, String imgUrl) {
        this.name = name;
        this.price = price;
        this.cnt = cnt;
        this.contents = contents;
        this.imgUrl = imgUrl;

        return this;
    }

    public static Product createProduct(Integer price, String name, Integer cnt, String contents, String imgUrl){
        Product product = new Product();
        product.price = price;
        product.name = name;
        product.cnt = cnt;
        product.contents = contents;
        product.imgUrl = imgUrl;

        return product;
    }
}
