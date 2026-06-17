package com.dessirestore.service_combos.repository;

import com.dessirestore.service_combos.model.DetalleCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleComboRepository extends JpaRepository<DetalleCombo, Long> {
}