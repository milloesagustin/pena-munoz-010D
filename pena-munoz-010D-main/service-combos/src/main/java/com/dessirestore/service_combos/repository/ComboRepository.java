package com.dessirestore.service_combos.repository;

import com.dessirestore.service_combos.model.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {
    List<Combo> findByEstaActivoTrue();
}