package com.imas.repository;

import com.imas.model.Crime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by spoko on 22.06.16.
 */
public interface CrimeRepository extends JpaRepository<Crime, Long> {
    @Query(value = "SELECT * FROM Crime c JOIN CrimeCategory cc ON c.crimeCategory_id = cc.id " +
            "WHERE cc.crimeName IN ?1", nativeQuery = true)
    List<Crime> findCrimesWithCategories(String[] categories);

    List<Crime> findByCrimeCategoryIdIn(long[] ids);
}
