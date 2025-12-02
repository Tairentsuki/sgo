package com.sgo.repository;

import com.sgo.dto.GraficoDTO;
import com.sgo.model.Obra;
import com.sgo.model.StatusObra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {

    // 1. Só Nome
    List<Obra> findByNomeContainingIgnoreCaseOrderByIdDesc(String nome);

    // 2. Só Status
    List<Obra> findByStatusOrderByIdDesc(StatusObra status);

    // 3. Nome E Status combinados
    List<Obra> findByNomeContainingIgnoreCaseAndStatusOrderByIdDesc(String nome, StatusObra status);

    // 4. Tudo
    List<Obra> findAllByOrderByIdDesc();

    // --- DASHBOARD ---
    long countByStatus(StatusObra status);

    @Query("SELECT new com.sgo.dto.GraficoDTO(CAST(o.status AS string), COUNT(o)) FROM Obra o GROUP BY o.status")
    List<GraficoDTO> obrasPorStatus();
}