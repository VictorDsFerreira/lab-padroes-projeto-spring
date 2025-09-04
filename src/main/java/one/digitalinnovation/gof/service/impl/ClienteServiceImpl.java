package one.digitalinnovation.gof.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author falvojr
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		return clienteRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: id=" + id));
	}

	@Override
	@Transactional
	public Cliente inserir(Cliente cliente) {
		return salvarClienteComCep(cliente);
	}

	@Override
	@Transactional
	public Cliente atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, caso exista:
		Cliente existente = clienteRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: id=" + id));
		cliente.setId(existente.getId());
		return salvarClienteComCep(cliente);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		if (!clienteRepository.existsById(id)) {
			throw new ResourceNotFoundException("Cliente não encontrado: id=" + id);
		}
		clienteRepository.deleteById(id);
	}

	private Cliente salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		return clienteRepository.save(cliente);
	}

}
