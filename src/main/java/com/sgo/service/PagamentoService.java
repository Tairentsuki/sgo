package com.sgo.service;

import com.sgo.model.Pagamento;
import com.sgo.model.RegistroTrabalho;
import com.sgo.repository.PagamentoRepository;
import com.sgo.repository.RegistroTrabalhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired private PagamentoRepository pagamentoRepo;
    @Autowired private RegistroTrabalhoRepository registroRepo;

    @Transactional
    public void efetuarPagamento(List<Long> idsRegistros) {
        List<RegistroTrabalho> registros = registroRepo.findAllById(idsRegistros);

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (RegistroTrabalho reg : registros) {
            if (reg.isPago()) throw new RuntimeException("Registro já pago: " + reg.getId());
            valorTotal = valorTotal.add(reg.getTotalCalculado());
        }

        // Gera recibo único para o lote
        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setDataPagamento(LocalDate.now());
        novoPagamento.setValorTotalPago(valorTotal);
        pagamentoRepo.save(novoPagamento);

        // Vincula registros ao recibo
        registros.forEach(r -> r.setPagamento(novoPagamento));
        registroRepo.saveAll(registros);
    }
}