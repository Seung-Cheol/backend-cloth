package project.store.cloth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.store.cloth.domain.ClothType;

public interface ClothTypeRepository extends JpaRepository<ClothType, Long> {

}
