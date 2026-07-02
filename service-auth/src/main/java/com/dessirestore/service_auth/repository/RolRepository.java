package com.dessirestore.service_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_auth.model.Rol;
import java.util.Optional;


@Repository
public interface RolRepository extends JpaRepository<Rol,Long>{

    Optional<Rol> findById(Long id);

}
