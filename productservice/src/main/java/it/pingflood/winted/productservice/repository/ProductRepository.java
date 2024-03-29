package it.pingflood.winted.productservice.repository;

import it.pingflood.winted.productservice.data.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>, QuerydslPredicateExecutor<Product> {
  List<Product> findAllByName(String name);
  
  List<Product> findAllByOwner(String owner);
  
  List<Product> findAllByBoughtIsFalseAndDraftIsFalse();
  
  List<Product> findAllByBoughtIsFalseAndDraftIsFalseAndOwnerIs(String owner);
  
}
