package io.github.fintechproject.msavaliadorcredito.application;

import io.github.fintechproject.msavaliadorcredito.application.domain.model.*;
import io.github.fintechproject.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.fintechproject.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import io.github.fintechproject.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity consultarSituacaoCliente(@RequestParam("cpf") String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
       try {
           SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
           return ResponseEntity.ok(situacaoCliente);
       } catch (DadosClienteNotFoundException e) {
           return ResponseEntity.notFound().build();
       } catch (ErroComunicacaoMicroservicesException e) {
           return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
       }
    }
    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }
    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try{
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService.solcitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        } catch (ErroSolicitacaoCartaoException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

