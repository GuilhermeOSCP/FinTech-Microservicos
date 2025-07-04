package io.github.fintechproject.msavaliadorcredito.application;

import feign.FeignException;
import io.github.fintechproject.msavaliadorcredito.application.domain.model.*;
import io.github.fintechproject.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.fintechproject.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import io.github.fintechproject.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import io.github.fintechproject.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.fintechproject.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.fintechproject.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele
    private final CartoesResourceClient cartoesClient; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            DadosCliente dadosCliente = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);

            return SituacaoCliente.builder()
                    .cliente(dadosCliente)
                    .cartoes(cartoesResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            DadosCliente dadosCliente = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesRespose = cartoesClient.getCartoesRendaAteh(renda);

            List<Cartao> cartoes = cartoesRespose.getBody();
            List<CartaoAprovado> listaCartoesAprovados = cartoes.stream().map(cartao -> {

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solcitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try{
            emissaoCartaoPublisher.solicitarCartao(dados);
            String protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
