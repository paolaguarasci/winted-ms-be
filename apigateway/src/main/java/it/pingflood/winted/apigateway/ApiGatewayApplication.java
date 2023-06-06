package it.pingflood.winted.apigateway;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
  
  
  private final RouteDefinitionLocator locator;
  
  public static void main(String[] args) {
    SpringApplication.run(it.pingflood.winted.apigateway.ApiGatewayApplication.class, args);
  }
  
  public ApiGatewayApplication(RouteDefinitionLocator locator) {
    this.locator = locator;
  }
  
  @Bean
  public List<GroupedOpenApi> apis() {
    List<GroupedOpenApi> groups = new ArrayList<>();
    List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
    assert definitions != null;
    definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
      String name = routeDefinition.getId().replaceAll("-service", "");
      groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
    });
    return groups;
  }
  
}
