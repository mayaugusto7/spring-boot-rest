package br.com.mayribeiro.service;

import java.util.List;

import br.com.mayribeiro.model.Todo;

public interface ITodoService {

	void createTodo(Todo todo);

	List<Todo> listarTodos();

	void removerTodo(Long id);

	Todo findById(Long id);

	Todo updateTodo(Todo currentTodo);

	boolean isTodoExist(Todo todo);

	void deleteAllTodos();

}
