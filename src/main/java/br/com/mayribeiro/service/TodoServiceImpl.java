package br.com.mayribeiro.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mayribeiro.model.Todo;
import br.com.mayribeiro.repository.TodoRepository;

@Service
public class TodoServiceImpl implements ITodoService {

	@Autowired
	private TodoRepository todoRepository;

	@Override
	public void createTodo(Todo todo) {
		todoRepository.save(todo);
	}

	@Override
	public List<Todo> listarTodos() {
		return todoRepository.findAll();
	}

	@Override
	public void removerTodo(Long id) {
		todoRepository.delete(id);
	}

	@Override
	public Todo findById(Long id) {
		return todoRepository.findOne(id);
	}

	@Override
	public Todo updateTodo(Todo currentTodo) {
		return todoRepository.save(currentTodo);
	}

	@Override
	public boolean isTodoExist(Todo todo) {
		return todoRepository.findByNome(todo.getNome()) != null;
	}

	@Override
	public void deleteAllTodos() {
		todoRepository.deleteAll();
	}
	
}
