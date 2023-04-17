package dev.fmatharm.delivery.persistence.repo;

import dev.fmatharm.delivery.persistence.data.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
