package io.github.fintechproject.mscartoes.infra.repository;

import io.github.fintechproject.mscartoes.domain.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CartaoRepository extends JpaRepository <Cartao, Long>{
    List<Cartao> findByRendaLessThanEqual(BigDecimal renda);
}
