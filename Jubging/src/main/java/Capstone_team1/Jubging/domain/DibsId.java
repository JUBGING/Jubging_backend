package Capstone_team1.Jubging.domain;

import java.io.Serializable;
import java.util.Objects;

public class DibsId implements Serializable {

    private User user;
    private Product product;

    public DibsId()
    {

    }
    public DibsId(User user, Product product)
    {
        this.user = user;
        this.product = product;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, product);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        DibsId dibsId = (DibsId) obj;
        return user.equals(dibsId.user) && product.equals(dibsId.product);
    }
}
