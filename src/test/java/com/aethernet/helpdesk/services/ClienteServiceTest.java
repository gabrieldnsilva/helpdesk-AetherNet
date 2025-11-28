package com.aethernet.helpdesk.services;

import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.dto.request.ClienteRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ClienteResponseDTO;
import com.aethernet.helpdesk.exceptions.DuplicateEntityException;
import com.aethernet.helpdesk.exceptions.EntityNotFoundException;
import com.aethernet.helpdesk.repositories.ClienteRepository;
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
@DisplayName("Testes do ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void deveCriarClienteComSucesso() {
        // Arrange
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "João Silva",
                "123.456.789-00",
                "joao@email.com",
                "senha123",
                null

        );

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(UUID.randomUUID());
        clienteSalvo.setNome(dto.nome());
        clienteSalvo.setCpf(dto.cpf());
        clienteSalvo.setEmail(dto.email());

        when(clienteRepository.existsByCpf(dto.cpf())).thenReturn(false);
        when(clienteRepository.existsByEmail(dto.email())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        // Act
        ClienteResponseDTO resultado = clienteService.criar(dto);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("João Silva");
        assertThat(resultado.cpf()).isEqualTo("123.456.789-00");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cliente com CPF duplicado")
    void deveLancarExcecaoAoCriarClienteComCpfDuplicado() {
        // Arrange
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "João Silva",
                "123.456.789-00",
                "joao@email.com",
                "senha123",
                null
        );

        when(clienteRepository.existsByCpf(dto.cpf())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> clienteService.criar(dto))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("CPF já cadastrado");

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarClientePorIdComSucesso() {
        // Arrange
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");
        cliente.setEmail("joao@email.com");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // Act
        ClienteResponseDTO resultado = clienteService.buscarPorId(id);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.nome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar cliente inexistente")
    void deveLancarExcecaoAoBuscarClienteInexistente() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> clienteService.buscarPorId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("Deve deletar cliente com sucesso")
    void deveDeletarClienteComSucesso() {
        // Arrange
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(id);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);

        // Act
        clienteService.deletar(id);

        // Assert
        verify(clienteRepository, times(1)).delete(cliente);
    }
}
