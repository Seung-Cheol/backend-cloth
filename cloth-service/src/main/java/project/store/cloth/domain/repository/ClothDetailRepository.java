package project.store.cloth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.store.cloth.domain.ClothDetail;

@Repository
public interface ClothDetailRepository extends JpaRepository<ClothDetail, Long> {

}
