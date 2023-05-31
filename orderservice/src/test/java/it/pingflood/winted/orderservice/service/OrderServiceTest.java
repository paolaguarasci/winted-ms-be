//package it.pingflood.winted.orderservice.service;
//
//import it.pingflood.winted.orderservice.data.Order;
//import it.pingflood.winted.orderservice.data.dto.OrderRequest;
//import it.pingflood.winted.orderservice.data.dto.OrderResponse;
//import it.pingflood.winted.orderservice.repository.OrderRepository;
//import it.pingflood.winted.orderservice.service.impl.OrderServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//
//  @Mock
//  private OrderRepository orderRepository;
//
//  @InjectMocks
//  private OrderServiceImpl orderService;
//
//  private Order order;
//  private OrderRequest orderRequest;
//
//  private OrderResponse orderResponse;
//
//  private OrderPutRequest orderPutRequest;
//
//  //  private UUID uuid_fromStringWrong;
//  private UUID uuid_random;
//
//  @BeforeEach
//  void setup() {
////    uuid_fromStringWrong = UUID.fromString("lkdjflkssdasdasdjdflksjdflksjdflkj");
//    uuid_random = UUID.randomUUID();
//
//    order = Order.builder().id(UUID.randomUUID()).user("ciccio").product("pr12345").build();
//
//    orderRequest = OrderRequest.builder().user("ciccio").product("pr12345").build();
//    orderResponse = OrderResponse.builder().id(UUID.randomUUID()).user("ciccio").product("pr12345").build();
//    orderPutRequest = OrderPutRequest.builder().id(UUID.randomUUID()).user("ciccio").product("pr12345").build();
//  }
//
//
//  @DisplayName("Save - return correct value")
//  @Test
//  void when_save_order_is_should_be_return_order() {
//    when(orderRepository.save(any(Order.class))).thenReturn(order);
//    OrderResponse orderResponse = orderService.createOrder(orderRequest);
//    assertThat(orderResponse.getProduct()).isSameAs(orderResponse.getProduct());
//  }
//
//  @DisplayName("Save - throw exception when duplicate unique field")
//  @Test
//  void when_try_to_save_whit_existing_name_product_throws_exception() {
//    when(orderRepository.findByUserAndProduct(orderRequest.getUser(), orderRequest.getProduct())).thenReturn(Optional.of(order));
//    assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderRequest));
//  }
//
//  @DisplayName("Get All - return correct value")
//  @Test
//  void when_get_orders_should_be_return_orders() {
//    when(orderRepository.findAll()).thenReturn(List.of(order, order, order));
//    List<OrderResponse> ordersResponse = orderService.getAll();
//    assertThat(ordersResponse)
//      .isInstanceOf(List.class)
//      .hasSize(3);
//    assertThat(ordersResponse.get(0).getUser())
//      .isEqualTo(order.getUser());
//  }
//
//  @DisplayName("Get All By User - return correct value")
//  @Test
//  void when_get_orders_by_user_should_be_return_orders() {
//    when(orderRepository.findAllByUser(order.getUser())).thenReturn(List.of(order, order, order));
//    List<OrderResponse> ordersResponse = orderService.getAllByUser(order.getUser());
//    assertThat(ordersResponse)
//      .isInstanceOf(List.class)
//      .hasSize(3);
//    assertThat(ordersResponse.get(0).getUser())
//      .isEqualTo(order.getUser());
//    assertThat(ordersResponse.get(0).getUser())
//      .isEqualTo(ordersResponse.get(1).getUser());
//    assertThat(ordersResponse.get(1).getUser())
//      .isEqualTo(ordersResponse.get(2).getUser());
//  }
//
//  @DisplayName("Get One - return correct value")
//  @Test
//  void when_get_order_should_be_return_order() {
//    when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(order));
//    OrderResponse orderResponse = orderService.getOne(UUID.randomUUID());
//    assertThat(orderResponse.getUser()).isEqualTo(orderResponse.getUser());
//  }
//
////  @DisplayName("Get One - throw exception when search not correct id")
////  @Test
////  void when_get_order_whit_wrong_id_should_throw() {
////    assertThrows(IllegalArgumentException.class, () -> orderService.getOne(uuid_fromStringWrong));
////  }
//
//  @DisplayName("Get One - throw exception when search not present id")
//  @Test
//  void when_get_order_whit_not_present_id_should_throw() {
//    when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
//    assertThrows(NoSuchElementException.class, () -> orderService.getOne(uuid_random));
//  }
//
////  @DisplayName("Put One - throw exception when try to change not correct id")
////  @Test
////  void when_put_order_whit_wrong_id_should_throw() {
////    assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(uuid_fromStringWrong, orderPutRequest));
////  }
//
//  @DisplayName("Put One - throw exception when try to change not present id")
//  @Test
//  void when_put_order_whit_not_present_id_should_throw() {
//    when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
//    UUID id = UUID.randomUUID();
//    orderPutRequest.setId(id);
//    assertThrows(NoSuchElementException.class, () -> orderService.updateOrder(id, orderPutRequest));
//  }
//
//  @DisplayName("Put One - throw exception when try to change mismatch id")
//  @Test
//  void when_put_order_whit_mismatch_id_should_throw() {
//    UUID id = UUID.randomUUID();
//    orderPutRequest.setId(id);
//    assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(uuid_random, orderPutRequest));
//  }
//
//
//  @DisplayName("Put One - update order then return it")
//  @Test
//  void when_put_order_whit_should_return_it() {
//    Order updatedOrder = Order.builder().id(orderPutRequest.getId()).user(orderPutRequest.getUser()).product(orderPutRequest.getProduct()).build();
//    order.setId(updatedOrder.getId());
//    when(orderRepository.save(updatedOrder)).thenReturn(order);
//    when(orderRepository.findById(orderPutRequest.getId())).thenReturn(Optional.of(order));
//    OrderResponse pr = orderService.updateOrder(orderPutRequest.getId(), orderPutRequest);
//    assertThat(pr.getUser()).isEqualTo(updatedOrder.getUser());
//  }
//
//
////  @DisplayName("Delete One - throw exception when try to delete not correct id")
////  @Test
////  void when_delete_order_whit_wrong_id_should_throw() {
////    assertThrows(IllegalArgumentException.class, () -> orderService.deleteOrder(uuid_fromStringWrong));
////  }
//
//  @DisplayName("Delete One - throw exception when try to delete not present id")
//  @Test
//  void when_delete_order_whit_not_present_id_should_throw() {
//    when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
//    UUID uuidRandom = UUID.randomUUID();
//    assertThrows(NoSuchElementException.class, () -> orderService.deleteOrder(uuidRandom));
//  }
//
////  @DisplayName("Delete One - delete element")
////  @Test
////  void when_delete_order_should_delete() {
////    orderService.deleteOrder("644d35690b56f0720b01f1a9");
////  }
//
//}
