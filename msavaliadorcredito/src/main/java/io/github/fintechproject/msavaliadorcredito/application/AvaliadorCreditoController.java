package io.github.fintechproject.msavaliadorcredito.application;

import io.github.fintechproject.msavaliadorcredito.application.domain.model.SituacaoCliente;
import io.github.fintechproject.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.fintechproject.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele

    @GetMapping
    public String status(){
        return "Ok";
    }
    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
       try {
           SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
           return ResponseEntity.ok(situacaoCliente);
       } catch (DadosClienteNotFoundException e) {
           return ResponseEntity.notFound().build();
       } catch (ErroComunicacaoMicroservicesException e) {
           return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
       }
    }
}

