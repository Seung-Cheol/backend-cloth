package project.store.cloth.domain.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.store.cloth.domain.Cloth;


@Repository
public interface ClothRepository extends JpaRepository<Cloth, Long> {

  @Query("SELECT c FROM Cloth c ORDER BY c.created_at")
  Page<Cloth> findAllLimit(Pageable pageable);

}
