package br.com.dacinho.movies.DTO.form;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientFormDTO {
	@NotNull @NotEmpty @Length(min=3)
	private String login;
	@NotNull @NotEmpty @Length(min=3)
	private String senha;
	@Autowired
	private BCryptPasswordEncoder pe;
	
	public ClientFormDTO(Client client) {
		this.login = client.getLogin();
		this.senha = client.getSenha();
	}
	public static List<ClientFormDTO> convert(List<Client> clients) {
		return clients.stream().map(ClientFormDTO::new).collect(Collectors.toList());
	}
	public Client convert() {
		pe = new BCryptPasswordEncoder();
		return new Client(this.login, pe.encode(this.senha));
	}
	public Client update(Long id, ClientRepository clientRepository) {
		// TODO Auto-generated method stub
		Client client = clientRepository.getOne(id);
		client.setLogin(this.login);
		client.setSenha(this.senha);
		
		return client;
	}
}
