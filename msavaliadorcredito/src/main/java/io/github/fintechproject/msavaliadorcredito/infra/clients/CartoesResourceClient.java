package io.github.fintechproject.msavaliadorcredito.infra.clients;

import io.github.fintechproject.msavaliadorcredito.application.domain.model.Cartao;
import io.github.fintechproject.msavaliadorcredito.application.domain.model.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscartoes", path = "/cartoes")
public interface CartoesResourceClient {
    @GetMapping(params = "cpf")
    ResponseEntity<List<CartaoCliente>> getCartoesByCliente(@RequestParam("cpf") String cpf);
    @GetMapping(params = "renda")
    ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda);
}
