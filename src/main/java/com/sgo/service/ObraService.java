package com.sgo.service;

import com.sgo.model.Obra;
import com.sgo.model.StatusObra;
import com.sgo.repository.ObraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ObraService {

    private final ObraRepository obraRepo;

    public ObraService(ObraRepository obraRepo) {
        this.obraRepo = obraRepo;
    }

    public List<Obra> listarTodas() {
        return obraRepo.findAllByOrderByIdDesc();
    }

    public List<Obra> listarComFiltro(String busca, StatusObra status) {
        boolean temBusca = busca != null && !busca.isBlank();

        if (temBusca && status != null) {
            return obraRepo.findByNomeContainingIgnoreCaseAndStatusOrderByIdDesc(busca, status);
        }
        if (temBusca) {
            return obraRepo.findByNomeContainingIgnoreCaseOrderByIdDesc(busca);
        }
        if (status != null) {
            return obraRepo.findByStatusOrderByIdDesc(status);
        }
        return obraRepo.findAllByOrderByIdDesc();
    }

    @Transactional
    public Obra salvar(Obra obra) {
        return obraRepo.save(obra);
    }

    public Obra buscarPorId(Long id) {
        return obraRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
    }
}
