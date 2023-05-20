package it.pingflood.winted.addressservice.config;

import it.pingflood.winted.addressservice.data.Address;
import it.pingflood.winted.addressservice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB implements CommandLineRunner {
  private final AddressRepository addressRepository;
  
  @Override
  public void run(String... args) throws Exception {
    log.info("Init db");
    addressRepository.saveAndFlush(Address.builder()
      .username("paola")
      .nome("Giovanna")
      .cognome("Rossi")
      .via("Via dei mille")
      .cap("00100")
      .citta("Roma")
      .numeroCivico("0")
      .build());
    log.info("Fine");
  }
}
