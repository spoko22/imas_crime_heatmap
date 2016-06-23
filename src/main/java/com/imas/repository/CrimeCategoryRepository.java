package com.imas.repository;

import com.imas.model.CrimeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by spoko on 22.06.16.
 */
public interface CrimeCategoryRepository extends JpaRepository<CrimeCategory, Long> {
}
