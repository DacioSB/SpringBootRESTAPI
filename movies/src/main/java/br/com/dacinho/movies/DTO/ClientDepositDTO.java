package br.com.dacinho.movies.DTO;

import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.repository.ClientRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDepositDTO {
	private double value;
	
	public Client update(Long id, ClientRepository clientRepository) {
		// TODO Auto-generated method stub
		Client client = clientRepository.getOne(id);
		client.deposit(this.value);
		
		return client;
	}
}
