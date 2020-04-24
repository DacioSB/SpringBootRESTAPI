package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.TopicoFormDTO;
import br.com.alura.forum.model.*;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	//Interface do repository
	@Autowired
	private TopicoRepository topicoRepository;
	@Autowired
	private CursoRepository cursoRepository;
	
	
	//REST
	//Recursos: Aluno, Topico, Resposta, Curso...
	//Nós identificamos cada recurso com uma URI
	//Ok... Como vamos manipular recursos(classes)
	//Como manipular funcoes desses recursos? Verbos HTTP!
	@GetMapping
	public List<TopicoDTO> lista(String nomeCurso){
		//Se o nome for nulo, eu passo todos os atribs pra ele
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDTO.converter(topicos);
		}
		//Se nao for nulo, eu vou pegar o curso na classe topico
		//Na classe curso vou pegar o nome
		//Usando essa nomenclatura finByCursoNome
		//http://localhost:8080/topicos?nomeCurso=HTML+5
		List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
		return TopicoDTO.converter(topicos);
	}
	
	//Preciso desse requestbody
	//Porque o metodo lista com get, ele pega a url
	//Mas esse parametro nao vem na url
	//Vem no corpo da requisicao porque é via metodo post
	//Ele faz o mesmo processo, so que agora com form
	//Ele ta pegando informaçao do usuario
	//<TopicoDTO> poderia ser topico, mas nao se trabalha com classes de dominio
	@PostMapping
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoFormDTO form, UriComponentsBuilder uriBuilder) {
		Topico topico =	form.converter(cursoRepository);
		this.topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
		
	}
}
