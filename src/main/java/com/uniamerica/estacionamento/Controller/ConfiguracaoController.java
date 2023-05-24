package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Configuracao;
import com.uniamerica.estacionamento.Respository.ConfiguracaoRepository;
import com.uniamerica.estacionamento.Service.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/configuracao")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    private ConfiguracaoService configuracaoService;
    @PostMapping
    public ResponseEntity<?> cadastrar (@RequestBody final Configuracao configuracao){
        try{
            this.configuracaoService.cadastrar(configuracao);
            return ResponseEntity.ok(configuracao);
        } catch (RuntimeException erro){
            return ResponseEntity.badRequest().body("Erro"+erro.getMessage());
        }
    }

    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){
        return ResponseEntity.ok(this.configuracaoRepository.findAll());
    }

    @PutMapping
    public ResponseEntity<?> editar (@RequestParam("id") final Long id, @RequestBody Configuracao configuracao){
        try{
            final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);

            if (configuracaoBanco == null || !configuracaoBanco.getId().equals(configuracao.getId())){
                throw new RuntimeException("Nao foi possivel identificar configuracao no banco de dados.");
            }

            this.configuracaoRepository.save(configuracao);
            return ResponseEntity.ok("Registro atualizado.");
        }catch (DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Erro" + erro.getCause().getCause().getMessage());
        }catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Erro" + erro.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete (@RequestParam("id") final Long id){
        final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);

        if (configuracaoBanco == null){
            return ResponseEntity.ok("Nenhum registro com esse ID encontrado.");
        }else{
            configuracaoBanco.setAtivo(false);
            this.configuracaoRepository.delete(configuracaoBanco);
            return ResponseEntity.ok("Ativo (configuracao) alterado para false e deletado");
        }
    }
}
