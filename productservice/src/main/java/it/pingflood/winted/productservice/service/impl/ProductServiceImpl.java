package it.pingflood.winted.productservice.service.impl;

import com.querydsl.core.types.Predicate;
import it.pingflood.winted.productservice.data.Product;
import it.pingflood.winted.productservice.data.QProduct;
import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;
import it.pingflood.winted.productservice.event.NewProductEvent;
import it.pingflood.winted.productservice.repository.ProductRepository;
import it.pingflood.winted.productservice.service.ProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
  
  public static final String EXTERNAL_UPLOAD_URL = "https://localhost:8443/api/v1/resource/image";
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;
  
  private final KafkaTemplate<String, NewProductEvent> kafkaTemplate;
  
  public ProductServiceImpl(ProductRepository productRepository, KafkaTemplate<String, NewProductEvent> kafkaTemplate) {
    this.productRepository = productRepository;
    this.kafkaTemplate = kafkaTemplate;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public ProductResponse getOne(String id) {
    ObjectId objectId = new ObjectId(id);
    return modelMapper.map(productRepository.findById(objectId.toString()).orElseThrow(), ProductResponse.class);
  }
  
  @Override
  public List<ProductResponse> getAll() {
    return productRepository.findAllByBoughtIsFalseAndDraftIsFalse().stream().map(product -> modelMapper.map(product, ProductResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<ProductResponse> search(String s) {
    QProduct qProduct = new QProduct("product");
    Predicate querySearch = qProduct
      .name.containsIgnoreCase(s)
      .or(qProduct.description.containsIgnoreCase(s))
      .and(qProduct.bought.isFalse())
      .and(qProduct.draft.isFalse());
    List<Product> products = (List<Product>) productRepository.findAll(querySearch);
    log.info("Trovati {} prodotti con i criteri di ricerca selezionati", products.size());
    return products.stream().map(product -> modelMapper.map(product, ProductResponse.class)).collect(Collectors.toList());
  }
  
  @SneakyThrows
  @Override
  public ProductResponse createProduct(ProductRequest productRequest, String owner, String jwtToken) {
    log.debug("RICHIESTA! " + productRequest.toString());
    
    Product newProduct = Product.builder()
      .name(productRequest.getName())
      .description(productRequest.getDescription())
      .preferred(0)
      .bought(false)
      .draft(productRequest.isDraft())
      .brand(productRequest.getBrand())
      .category(productRequest.getCategory())
      .owner(owner)
      .price(productRequest.getPrice())
      .build();
    
    if (productRequest.getFiles() != null) {
      MultipartBodyBuilder builder = new MultipartBodyBuilder();
      MultipartFile[] files = productRequest.getFiles();
      for (MultipartFile file : files) {
        String headerFile = String.format("form-data; name=%s; filename=%s", "files", file.getOriginalFilename());
        builder.part("files", new ByteArrayResource(file.getBytes()), MediaType.IMAGE_PNG).header("Content-Disposition", headerFile);
      }
      
      MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();
      log.debug("TOKEN {}", jwtToken);
      log.debug("PRINCIPAL {}", owner);
      List<Map<String, Object>> resp;
      try {
        resp = WebClient.builder().build().post()
          .uri(EXTERNAL_UPLOAD_URL)
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .header("Authorization", "Bearer " + jwtToken)
          .body(BodyInserters.fromMultipartData(multipartBody))
          .exchangeToMono(clientResponse -> clientResponse.bodyToMono(List.class)).block();
        
        if (resp == null) throw new IllegalArgumentException("Errore nel caricamento delle immagini");
        
        log.debug("================ RESPONSE =================== ");
        log.debug("{}", resp);
        
      } catch (Exception ignored) {
        log.error("PROBLEMA {}", ignored.getMessage());
        throw new IllegalArgumentException("Errore nel salvataggio del prodotto.");
      }
      
      List<String> ids = new ArrayList<>();
      
      for (Map<String, Object> img : resp) {
        ids.add(img.get("id").toString());
      }
      newProduct.setResources(ids);
    }
    
    Product savedProduct = productRepository.save(newProduct);
    kafkaTemplate.send("NewProduct", "product-service", new NewProductEvent(savedProduct.getId()));
    return modelMapper.map(savedProduct, ProductResponse.class);
    
  }
  
  @SneakyThrows
  @Override
  public ProductResponse updateProduct(String id, ProductPutRequest productPutRequest) {
    ObjectId objectId = new ObjectId(id);
    if (!productPutRequest.getId().equals(objectId.toString())) throw new IllegalArgumentException();
    Product oldProduct = productRepository.findById(objectId.toString()).orElseThrow();

    oldProduct.setName(productPutRequest.getName());
    oldProduct.setDescription(productPutRequest.getDescription());
    oldProduct.setPrice(productPutRequest.getPrice());
    oldProduct.setDraft(productPutRequest.isDraft());
    return modelMapper.map(productRepository.save(oldProduct), ProductResponse.class);
  }
  
  @Override
  public void deleteProduct(String id, String name) {
    Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Wrong"));
    if (!product.getOwner().equals(name) ||
      !product.isDraft() ||
      product.isBought()) {
      throw new IllegalArgumentException("Wrong");
    }
    productRepository.delete(product);
  }
  
  @Override
  public void addPreferred(String productId) {
    Product product = productRepository.findById(productId).orElseThrow();
    product.setPreferred(product.getPreferred() + 1);
    productRepository.save(product);
  }
  
  @Override
  public void removePreferred(String productId) {
    Product product = productRepository.findById(productId).orElseThrow();
    if (product.getPreferred() > 0) {
      product.setPreferred(product.getPreferred() - 1);
      productRepository.save(product);
    }
  }
  
  @Override
  public List<ProductResponse> getAllByOwnerId(String ownerId) {
    return productRepository.findAllByOwner(ownerId).stream().map(product -> modelMapper.map(product, ProductResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<ProductResponse> getAllPublicByOwnerId(String ownerId) {
    return productRepository.findAllByBoughtIsFalseAndDraftIsFalseAndOwnerIs(ownerId).stream().map(product -> modelMapper.map(product, ProductResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<ProductResponse> getSameByProdId(String prodid) {
    // TODO logica implementativa... la similitudine in base a cosa???
    return productRepository.findAllByBoughtIsFalseAndDraftIsFalse().stream().map(product -> modelMapper.map(product, ProductResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public ProductResponse makeBought(String productId) {
    Product p = productRepository.findById(productId).orElseThrow();
    p.setBought(true);
    return modelMapper.map(productRepository.save(p), ProductResponse.class);
  }
  
  @Override
  public ProductResponse undoBought(String productId) {
    Product p = productRepository.findById(productId).orElseThrow();
    p.setBought(false);
    return modelMapper.map(productRepository.save(p), ProductResponse.class);
  }
}
