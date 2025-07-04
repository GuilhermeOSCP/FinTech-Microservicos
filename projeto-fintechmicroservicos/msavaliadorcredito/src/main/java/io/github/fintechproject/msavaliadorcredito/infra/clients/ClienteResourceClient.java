package io.github.fintechproject.msavaliadorcredito.infra.clients;

import io.github.fintechproject.msavaliadorcredito.application.domain.model.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "msclientes", path = "/clientes")
public interface ClienteResourceClient {

    @GetMapping(params = "cpf")
    DadosCliente dadosCliente(@RequestParam("cpf") String cpf);
}


