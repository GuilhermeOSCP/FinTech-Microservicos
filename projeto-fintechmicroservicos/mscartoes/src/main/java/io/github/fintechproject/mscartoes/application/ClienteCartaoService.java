package io.github.fintechproject.mscartoes.application;

import io.github.fintechproject.mscartoes.domain.ClienteCartao;
import io.github.fintechproject.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {
    private final ClienteCartaoRepository repository; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele

    public List<ClienteCartao> listCartoesByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
