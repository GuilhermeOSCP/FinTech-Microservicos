package io.github.fintechproject.msclientes.application;

import com.sun.istack.logging.Logger;
import io.github.fintechproject.msclientes.application.representation.ClienteSaveRequest;
import io.github.fintechproject.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
@Slf4j
public class ClientesResource {

    private final ClienteService service; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele

    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservice de clientes"); //Ignorar o erro, anotacao '@Slf4j' cobre ele
        return "Ok";
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ClienteSaveRequest request) {
        Cliente cliente = request.toModel();
        service.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<?> dadosCliente(@RequestParam("cpf") String cpf) {
        Optional<Cliente> cliente = service.getByCPF(cpf);
        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}