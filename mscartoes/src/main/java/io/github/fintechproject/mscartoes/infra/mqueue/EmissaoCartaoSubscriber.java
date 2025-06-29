package io.github.fintechproject.mscartoes.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fintechproject.mscartoes.domain.Cartao;
import io.github.fintechproject.mscartoes.domain.ClienteCartao;
import io.github.fintechproject.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import io.github.fintechproject.mscartoes.infra.repository.CartaoRepository;
import io.github.fintechproject.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele
    private final ClienteCartaoRepository clienteCartaoRepository; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload){
        try{
            var mapper = new ObjectMapper();

            DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            Cartao cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();

            ClienteCartao clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimite(dados.getLimiteLiberado());

            clienteCartaoRepository.save(clienteCartao);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
