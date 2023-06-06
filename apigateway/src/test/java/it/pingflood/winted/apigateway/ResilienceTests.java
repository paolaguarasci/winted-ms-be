//package it.pingflood.winted.apigateway;
//
//
//import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
//import com.carrotsearch.junitbenchmarks.BenchmarkRule;
//import it.pingflood.winted.apigateway.dto.ProductResponse;
//import org.junit.BeforeClass;
//import org.junit.ClassRule;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.rules.TestRule;
//import org.junit.runner.RunWith;
//import org.mockserver.client.MockServerClient;
//import org.mockserver.matchers.Times;
//import org.mockserver.model.HttpRequest;
//import org.mockserver.springtest.MockServerTest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.MockServerContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.utility.DockerImageName;
//
//import java.util.Base64;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.Assert.assertTrue;
//import static org.mockserver.model.HttpResponse.response;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//  properties = {"rateLimiter.secure=true"})
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//@MockServerTest
//public class ResilienceTests {
//  private static final Logger LOGGER = LoggerFactory.getLogger(ResilienceTests.class);
//
//  @Container
//  public static MockServerContainer mockServer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));
//
//  @Autowired
//  TestRestTemplate template;
//
//  @BeforeAll
//  public static void init() {
//    System.setProperty("spring.cloud.gateway.httpclient.response-timeout", "100ms");
//    System.setProperty("spring.cloud.gateway.routes[0].id", "account-service");
//    System.setProperty("spring.cloud.gateway.routes[0].uri", "http://" + mockServer.getHost() + ":" + mockServer.getServerPort());
//    System.setProperty("spring.cloud.gateway.routes[0].predicates[0]", "Path=/accounts/**");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[0]", "RewritePath=/accounts/(?<path>.*), /$\\{path}");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].name", "Retry");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].args.retries", "10");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].args.statuses", "INTERNAL_SERVER_ERROR");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].args.backoff.firstBackoff", "50ms");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].args.backoff.maxBackoff", "500ms");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].args.backoff.factor", "2");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].args.backoff.basedOnPreviousValue", "true");
//    System.setProperty("spring.cloud.gateway.routes[0].filters[1].args.fallbackUri", "null");
//    MockServerClient client = new MockServerClient(mockServer.getContainerIpAddress(), mockServer.getServerPort());
//    client.when(HttpRequest.request()
//        .withPath("/1"), Times.exactly(3))
//      .respond(response()
//        .withStatusCode(500)
//        .withBody("{\"errorCode\":\"5.01\"}")
//        .withHeader("Content-Type", "application/json"));
//    client.when(HttpRequest.request()
//        .withPath("/1"))
//      .respond(response()
//        .withBody("{\"id\":1,\"number\":\"1234567891\"}")
//        .withHeader("Content-Type", "application/json"));
//    client.when(HttpRequest.request()
//        .withPath("/2"))
//      .respond(response()
//        .withBody("{\"id\":2,\"number\":\"1234567891\"}")
//        .withDelay(TimeUnit.MILLISECONDS, 200)
//        .withHeader("Content-Type", "application/json"));
//  }
//
//  @Test
//  public void testAccountService() {
//    LOGGER.info("Sending /1...");
//    ResponseEntity<String> r = template.exchange("/accounts/{id}", HttpMethod.GET, null, String.class, 1);
//    LOGGER.info("Received: status->{}, payload->{}", r.getStatusCode().value(), r.getBody());
//    assertTrue(r.getStatusCode().is2xxSuccessful());
//  }
//
//  @Test
//  public void testAccountServiceFail() {
//    LOGGER.info("Sending /2...");
//    ResponseEntity<String> r = template.exchange("/accounts/{id}", HttpMethod.GET, null, String.class, 2);
//    LOGGER.info("Received: status->{}, payload->{}", r.getStatusCode().value(), r.getBody());
//    assertTrue(r.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(504)));
//  }
//
//
//
//}
