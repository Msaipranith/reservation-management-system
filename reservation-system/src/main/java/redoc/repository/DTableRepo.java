package redoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import redoc.entity.DTable;

@Repository
public interface DTableRepo extends JpaRepository<DTable, Long> {

}
