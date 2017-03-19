package br.com.mayribeiro.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.mayribeiro.exceptions.CustomErrorType;
import br.com.mayribeiro.model.Todo;
import br.com.mayribeiro.service.ITodoService;

/**
 * GET AND POST COLOCAR http://localhost:8080/spring-rest/rest/api/v1/todo/
 * @author Maycon Augusto Ribeiro Guimarães <maycon_ribiero@hotmail.com>
 * @date 18 de mar de 2017
 */
@RestController
@RequestMapping("/rest/api/v1/")
public class TodoResourcesMobile {
	
	public static final Logger logger = LoggerFactory.getLogger(TodoResourcesMobile.class);
	
	@Autowired
	private ITodoService todoService;
	
	@RequestMapping(value = "/todo/", method = RequestMethod.POST)
	public ResponseEntity<?> createTodo(@RequestBody Todo todo) {

		logger.info("Criando novo Todo : {}", todo);
		
		if (todoService.isTodoExist(todo)) {
			
			logger.error("Já existe um Todo criado com esse nome: ", todo.getNome());
			
            return new ResponseEntity<String>(new CustomErrorType("Já existe um Todo criado com esse nome: " + 
            todo.getNome() + ".").toString(), HttpStatus.CONFLICT);
		}
		
		todo.setFechado(false);
		todoService.createTodo(todo);
		
		//HttpHeaders headers = new HttpHeaders();
	    //headers.setLocation(ucBuilder.path("/rest/api/v1/todo/{id}").buildAndExpand(todo.getId()).toUri());

		return new ResponseEntity<String>("Objeto Criado", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/todo/", method = RequestMethod.GET)
	public ResponseEntity<List<Todo>> listarTodos() {
		
		logger.info("Listando Todos : ");
		
		List<Todo> listaTodos = todoService.listarTodos();
		
		if (listaTodos.isEmpty()) {
			return new ResponseEntity<List<Todo>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Todo>>(listaTodos, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> buscarTodo(@PathVariable("id") Long id) {
		
		logger.info("Buscando todo..");
		
		Todo todo = todoService.findById(id);
		
		if (todo == null) {
			return new ResponseEntity<String>("Todo nao encontrado!", HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Todo>(todo, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> removerTodo(@PathVariable("id") Long id) {
		
		logger.info("Removendo Todo com id {}", id);
		
		Todo todo = todoService.findById(id);
		
		if (todo == null) {
			 logger.error("Remover não realizado. Todo com id {} nao encontrado.", id);
	            
	            return new ResponseEntity<String>(new CustomErrorType("Remover não realizado.  Todo com id " + id + " nao encontrado.").toString(),
	                    HttpStatus.NOT_FOUND);
		}
		
		todoService.removerTodo(id);
		
		return new ResponseEntity<String>("Todo removido com sucesso", HttpStatus.OK);
	}

	@RequestMapping(value = "/todo/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> alterarTodo(@PathVariable("id") Long id, @RequestBody Todo todo) {
		 
			logger.info("Atualizando Todo com id {}", id);
		 
	        Todo currentTodo = todoService.findById(id);
	 
	        if (currentTodo == null) {
	        	
	            logger.error("Update não realizado. Todo com id {} nao encontrado.", id);
	            
	            return new ResponseEntity<String>(new CustomErrorType("Update não realizado.  Todo com id " + id + " nao encontrado.").toString(),
	                    HttpStatus.NOT_FOUND);
	        }
	 
	        currentTodo.setNome(todo.getNome());
	        currentTodo.setDescricao(todo.getDescricao());
	        currentTodo.setFechado(todo.getFechado());
	        
	        todoService.updateTodo(currentTodo);
	        
	        return new ResponseEntity<Todo>(currentTodo, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/todo/", method = RequestMethod.DELETE)
	public ResponseEntity<Todo> deleteAllTodos() {
		
		logger.info("Deletando all todos.");
		
		todoService.deleteAllTodos();
		
		return new ResponseEntity<Todo>(HttpStatus.NO_CONTENT);
	}
}

