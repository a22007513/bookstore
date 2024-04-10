package rocky.com.example.virtualbookstore.repository;

import org.springframework.data.repository.CrudRepository;
import rocky.com.example.virtualbookstore.module.orderItem;

public interface orderItemRepository extends CrudRepository<orderItem, Integer> {


}
