package io.github.fintechproject.mscartoes.application.representation;

import io.github.fintechproject.mscartoes.domain.BandeiraCartao;
import io.github.fintechproject.mscartoes.domain.Cartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoSaveRequest {
    private String nome;
    private String bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel(){
        return new Cartao(nome, BandeiraCartao.fromString(bandeira), renda, limite);
    }
}
