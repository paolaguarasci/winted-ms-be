package it.pingflood.winted.messageservice.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Getter
@Setter
@RequiredArgsConstructor
@ToString
public abstract class Auditable<T> {
  @CreatedDate
  protected LocalDateTime creationDate;
  @LastModifiedDate
  protected LocalDateTime lastModifiedDate;
  @CreatedBy
  protected T createdBy;
  @LastModifiedBy
  protected T modifiedBy;
}
