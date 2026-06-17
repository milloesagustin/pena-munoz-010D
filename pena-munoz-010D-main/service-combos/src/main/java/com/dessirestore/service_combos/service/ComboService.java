package com.dessirestore.service_combos.service;

import com.dessirestore.service_combos.model.Combo;
import com.dessirestore.service_combos.repository.ComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComboService {

    @Autowired
    private ComboRepository comboRepository;

    public Combo crearCombo(Combo combo) {
        combo.setEstaActivo(true);
        
        // Amarrar la relación bidireccional antes de guardar
        if (combo.getProductosIncluidos() != null) {
            combo.getProductosIncluidos().forEach(detalle -> detalle.setCombo(combo));
        }
        
        return comboRepository.save(combo);
    }

    public List<Combo> listarCombosActivos() {
        return comboRepository.findByEstaActivoTrue();
    }

    public Combo cambiarEstadoCombo(Long id, Boolean estado) {
        return comboRepository.findById(id).map(combo -> {
            combo.setEstaActivo(estado);
            return comboRepository.save(combo);
        }).orElseThrow(() -> new RuntimeException("Combo no encontrado"));
    }
}