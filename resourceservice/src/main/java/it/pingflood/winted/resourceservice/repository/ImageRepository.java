package it.pingflood.winted.resourceservice.repository;

import it.pingflood.winted.resourceservice.data.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID>, JpaSpecificationExecutor<Image> {
}
