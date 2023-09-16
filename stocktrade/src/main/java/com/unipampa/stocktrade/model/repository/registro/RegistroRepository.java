package com.unipampa.stocktrade.model.repository.registro;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unipampa.stocktrade.model.entity.registro.Registro;

public interface RegistroRepository extends JpaRepository<Registro, Integer> {
    
}
