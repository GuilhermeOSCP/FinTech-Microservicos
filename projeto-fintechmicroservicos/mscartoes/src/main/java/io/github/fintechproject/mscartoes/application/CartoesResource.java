package io.github.fintechproject.mscartoes.application;

import io.github.fintechproject.mscartoes.application.representation.CartaoSaveRequest;
import io.github.fintechproject.mscartoes.application.representation.CartoesPorClienteResponse;
import io.github.fintechproject.mscartoes.domain.Cartao;
import io.github.fintechproject.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesResource {

    private final CartaoService cartaoService; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele
    private final ClienteCartaoService clienteCartaoService; //Ignorar o erro, anotacao '@RequiredArgsConstructor' cobre ele
    @GetMapping
    public String status() {
        return "Ok";
    }
    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request){
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda) {
        List<Cartao> list = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(list);
    }
    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf) {
       List<ClienteCartao> list = clienteCartaoService.listCartoesByCpf(cpf);
       List<CartoesPorClienteResponse> resultList = list.stream()
               .map(CartoesPorClienteResponse::fromModel)
               .collect(Collectors.toList());
       return ResponseEntity.ok(resultList);
    }
}
