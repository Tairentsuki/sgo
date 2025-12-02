package com.sgo.config;

import com.sgo.model.Cliente;
import com.sgo.model.Funcionario;
import com.sgo.model.Obra;
import com.sgo.model.Pagamento;
import com.sgo.model.RegistroTrabalho;
import com.sgo.model.StatusObra;
import com.sgo.repository.ClienteRepository;
import com.sgo.repository.FuncionarioRepository;
import com.sgo.repository.ObraRepository;
import com.sgo.repository.PagamentoRepository;
import com.sgo.repository.RegistroTrabalhoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepo;
    private final FuncionarioRepository funcionarioRepo;
    private final ObraRepository obraRepo;
    private final PagamentoRepository pagamentoRepo;
    private final RegistroTrabalhoRepository registroRepo;

    @PersistenceContext
    private EntityManager entityManager;

    private static final LocalDate HOJE = LocalDate.now();

    public DataSeeder(ClienteRepository clienteRepo,
                      FuncionarioRepository funcionarioRepo,
                      ObraRepository obraRepo,
                      PagamentoRepository pagamentoRepo,
                      RegistroTrabalhoRepository registroRepo) {
        this.clienteRepo = clienteRepo;
        this.funcionarioRepo = funcionarioRepo;
        this.obraRepo = obraRepo;
        this.pagamentoRepo = pagamentoRepo;
        this.registroRepo = registroRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (funcionarioRepo.count() > 0) {
            System.out.println("--- BANCO JÁ POPULADO. Execução da limpeza ignorada. ---");
            return;
        }

        limparBanco();
        popularDados();

        System.out.println("--- POPULAÇÃO DE DADOS TESTE CONCLUÍDA COM SUCESSO ---");
    }

    private void limparBanco() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE registro_trabalho").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE historico_valor_hora").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE pagamento").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE obra").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE funcionario").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE cliente").executeUpdate();

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    private void popularDados() {
        Cliente cli1 = novoCliente("Shopping Center Plaza", "12345678000190", "Av. Nações, 1000");
        Cliente cli2 = novoCliente("Condomínio Jardins", "98765432000110", "Rua das Flores, 50");
        Cliente cli3 = novoCliente("Construtora Horizonte", "11222333000100", "Rodovia BR 101, Km 50");
        Cliente cli4 = novoCliente("Dr. Roberto Mendes", "11122233344", "Rua da Praia, 200");

        Funcionario func1 = novoFuncionario("Carlos Engenheiro", "Engenheiro Civil", "10000000001",
                LocalDate.of(2023, 1, 10), BigDecimal.valueOf(150.00));
        Funcionario func2 = novoFuncionario("Mariana Arquiteta", "Arquiteta", "10000000002",
                LocalDate.of(2023, 3, 15), BigDecimal.valueOf(130.00));
        Funcionario func3 = novoFuncionario("João Mestre", "Mestre de Obras", "10000000003",
                LocalDate.of(2023, 2, 1), BigDecimal.valueOf(70.00));
        Funcionario func4 = novoFuncionario("Pedro Pedreiro", "Pedreiro", "10000000004",
                LocalDate.of(2023, 5, 20), BigDecimal.valueOf(40.00));
        Funcionario func5 = novoFuncionario("Lucas Servente", "Servente", "10000000006",
                LocalDate.of(2024, 1, 10), BigDecimal.valueOf(25.00));
        Funcionario func6 = novoFuncionario("Beto Eletricista", "Eletricista", "10000000009",
                LocalDate.of(2023, 11, 11), BigDecimal.valueOf(60.00));
        Funcionario func7 = novoFuncionario("Zé Encanador", "Encanador", "10000000010",
                LocalDate.of(2023, 10, 10), BigDecimal.valueOf(55.00));

        Obra obra1 = novaObra("Expansão Shopping Plaza", "Av. Nações, 1000", StatusObra.ANDAMENTO, cli1);
        Obra obra2 = novaObra("Reforma Piscina Condomínio", "Rua das Flores, 50", StatusObra.ANDAMENTO, cli2);
        Obra obra3 = novaObra("Consultório Dr. Roberto", "Centro Médico Sala 10", StatusObra.ANDAMENTO, cli4);
        Obra obra4 = novaObra("Fundação Prédio B", "Rodovia BR 101", StatusObra.ANDAMENTO, cli3);

        Pagamento pgto1 = novoPagamento(HOJE.minusDays(45), BigDecimal.valueOf(8500.00));
        Pagamento pgto2 = novoPagamento(HOJE.minusDays(15), BigDecimal.valueOf(4200.00));

        registroRepo.save(criarRegistro(HOJE.minusDays(60), 8.0, func1, obra1, "Planejamento estrutural", pgto1));
        registroRepo.save(criarRegistro(HOJE.minusDays(59), 8.0, func1, obra1, "Revisão de planta", pgto1));
        registroRepo.save(criarRegistro(HOJE.minusDays(58), 8.0, func2, obra1, "Design de interiores", pgto1));
        registroRepo.save(criarRegistro(HOJE.minusDays(50), 8.0, func3, obra1, "Gestão de equipe", pgto1));
        registroRepo.save(criarRegistro(HOJE.minusDays(49), 9.0, func4, obra1, "Demolição parede", pgto1));

        registroRepo.save(criarRegistro(HOJE.minusDays(16), 8.0, func4, obra2, "Reboco da piscina", pgto2));
        registroRepo.save(criarRegistro(HOJE.minusDays(16), 8.0, func5, obra2, "Limpeza do local", pgto2));

        registroRepo.save(criarRegistro(HOJE.minusDays(7), 8.0, func1, obra1, "Medição final de obra", null));
        registroRepo.save(criarRegistro(HOJE.minusDays(7), 8.0, func2, obra1, "Ajuste luminotécnico", null));
        registroRepo.save(criarRegistro(HOJE.minusDays(6), 8.0, func3, obra1, "Compra material", null));
        registroRepo.save(criarRegistro(HOJE.minusDays(6), 9.0, func6, obra1, "Quadro de força", null));

        registroRepo.save(criarRegistro(HOJE.minusDays(2), 8.0, func4, obra2, "Assentamento de azulejo", null));
        registroRepo.save(criarRegistro(HOJE.minusDays(1), 8.0, func5, obra2, "Preparo de massa", null));
        registroRepo.save(criarRegistro(HOJE, 4.0, func6, obra3, "Instalação de tomadas", null));
        registroRepo.save(criarRegistro(HOJE, 4.0, func4, obra3, "Retoques pintura", null));
    }

    private Cliente novoCliente(String nome, String cpf, String endereco) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setEndereco(endereco);
        return clienteRepo.save(cliente);
    }

    private Funcionario novoFuncionario(String nome, String profissao, String cpf,
                                        LocalDate admissao, BigDecimal valorHora) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nome);
        funcionario.setProfissao(profissao);
        funcionario.setCpf(cpf);
        funcionario.setDataAdmissao(admissao);
        funcionario.setValorHoraAtual(valorHora);
        return funcionarioRepo.save(funcionario);
    }

    private Obra novaObra(String nome, String endereco, StatusObra status, Cliente cliente) {
        Obra obra = new Obra();
        obra.setNome(nome);
        obra.setEndereco(endereco);
        obra.setStatus(status);
        obra.setCliente(cliente);
        return obraRepo.save(obra);
    }

    private Pagamento novoPagamento(LocalDate data, BigDecimal valor) {
        Pagamento pagamento = new Pagamento();
        pagamento.setDataPagamento(data);
        pagamento.setValorTotalPago(valor);
        return pagamentoRepo.save(pagamento);
    }

    private RegistroTrabalho criarRegistro(LocalDate data,
                                           double horas,
                                           Funcionario funcionario,
                                           Obra obra,
                                           String observacao,
                                           Pagamento pagamento) {
        RegistroTrabalho registro = new RegistroTrabalho();
        registro.setData(data);
        registro.setHoras(BigDecimal.valueOf(horas));
        registro.setFuncionario(funcionario);
        registro.setObra(obra);
        registro.setObservacao(observacao);
        registro.setPagamento(pagamento);

        registro.setValorHoraSnapshot(funcionario.getValorHoraAtual());
        registro.setTotalCalculado(registro.getHoras().multiply(registro.getValorHoraSnapshot()));

        return registro;
    }
}
