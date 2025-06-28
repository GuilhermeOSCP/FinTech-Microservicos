package io.github.fintechproject.msavaliadorcredito.application;

import io.github.fintechproject.msavaliadorcredito.application.domain.model.CartaoCliente;
import io.github.fintechproject.msavaliadorcredito.application.domain.model.DadosCliente;
import io.github.fintechproject.msavaliadorcredito.application.domain.model.SituacaoCliente;
import io.github.fintechproject.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.fintechproject.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele
    private final CartoesResourceClient cartoesClient; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele

    public SituacaoCliente obterSituacaoCliente(String cpf) {
        DadosCliente dadosCliente = clientesClient.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesResponse =  cartoesClient.getCartoesByCliente(cpf);

        return SituacaoCliente.builder()
                .cliente(dadosCliente)
                .cartoes(cartoesResponse.getBody())
                .build();
    }

}
