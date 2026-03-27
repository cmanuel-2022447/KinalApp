package com.cristianmanuel.Kinalapp.repository;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}