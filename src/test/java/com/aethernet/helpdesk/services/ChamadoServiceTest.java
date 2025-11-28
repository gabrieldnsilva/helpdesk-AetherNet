package com.aethernet.helpdesk.services;

import com.aethernet.helpdesk.domain.Chamado;
import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.Tecnico;
import com.aethernet.helpdesk.domain.dto.request.ChamadoRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ChamadoResponseDTO;
import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;
import com.aethernet.helpdesk.exceptions.DomainRuleException;
import com.aethernet.helpdesk.exceptions.EntityNotFoundException;
import com.aethernet.helpdesk.repositories.ChamadoRepository;
import com.aethernet.helpdesk.repositories.ClienteRepository;
import com.aethernet.helpdesk.repositories.TecnicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ChamadoService")
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private ChamadoService chamadoService;

    @Test
    @DisplayName("Deve abrir chamado com sucesso")
    void deveAbrirChamadoComSucesso() {
        // Arrange
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNome("João Silva");

        ChamadoRequestDTO dto = new ChamadoRequestDTO(
                Prioridade.ALTA,
                "Impressora não está imprimindo",
                "Impressora modelo XYZ",
                clienteId,
                null
        );

        Chamado chamadoSalvo = new Chamado();
        chamadoSalvo.setId(UUID.randomUUID());
        chamadoSalvo.setTitulo(dto.titulo());
        chamadoSalvo.setObservacoes(dto.observacoes());
        chamadoSalvo.setPrioridade(dto.prioridade());
        chamadoSalvo.setStatus(Status.ABERTO);
        chamadoSalvo.setCliente(cliente);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(chamadoSalvo);

        // Act
        ChamadoResponseDTO resultado = chamadoService.abrir(dto);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.status()).isEqualTo(Status.ABERTO);
        assertThat(resultado.nomeCliente()).isEqualTo("João Silva");
        verify(chamadoRepository, times(1)).save(any(Chamado.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fechar chamado ABERTO diretamente")
    void deveLancarExcecaoAoFecharChamadoAbertoDiretamente() {
        // Arrange
        UUID id = UUID.randomUUID();
        Chamado chamado = new Chamado();
        chamado.setId(id);
        chamado.setStatus(Status.ABERTO);

        when(chamadoRepository.findById(id)).thenReturn(Optional.of(chamado));

        // Act & Assert
        assertThatThrownBy(() -> chamadoService.fechar(id))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("apenas chamados EM_ANDAMENTO");
    }

    @Test
    @DisplayName("Deve atribuir técnico e mudar status para EM_ANDAMENTO automaticamente")
    void deveAtribuirTecnicoEMudarStatusAutomaticamente() {
        // Arrange
        UUID chamadoId = UUID.randomUUID();
        UUID tecnicoId = UUID.randomUUID();

        Chamado chamado = new Chamado();
        chamado.setId(chamadoId);
        chamado.setStatus(Status.ABERTO);

        Tecnico tecnico = new Tecnico();
        tecnico.setId(tecnicoId);
        tecnico.setNome("Carlos Tech");

        when(chamadoRepository.findById(chamadoId)).thenReturn(Optional.of(chamado));
        when(tecnicoRepository.findById(tecnicoId)).thenReturn(Optional.of(tecnico));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(chamado);

        // Act
        ChamadoResponseDTO resultado = chamadoService.atribuirTecnico(chamadoId, tecnicoId);

        // Assert
        assertThat(resultado.status()).isEqualTo(Status.EM_ANDAMENTO);
        assertThat(resultado.nomeTecnico()).isEqualTo("Carlos Tech");
    }
}
