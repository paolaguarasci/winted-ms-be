package it.pingflood.winted.paymentservice.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@RequiredArgsConstructor
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {
  
  @Column(name = "created_date", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  protected LocalDateTime creationDate;
  
  
  @Column(name = "lastMod_date")
  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  protected LocalDateTime lastModifiedDate;
  
  @CreatedBy
  @Column(name = "created_by")
  protected T createdBy;
  
  @LastModifiedBy
  @Column(name = "modified_by")
  protected T modifiedBy;
  
}
