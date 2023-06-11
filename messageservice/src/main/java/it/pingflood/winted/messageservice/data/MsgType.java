package it.pingflood.winted.messageservice.data;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum MsgType {
  @JsonEnumDefaultValue
  TESTO,
  IMG,
  DOMANDA,
  SYSTEM,
  OFFERT_REQUEST,
  OFFERT_RESPONSE,
  SYSTEM_REQUEST
}
