package fact.it.todolistservice.repository;

import fact.it.todolistservice.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<TodoList, Integer> {
    TodoList findTodoListByNaam(String Naam);
    List<TodoList> findListsByCategoryContaining(String category);
}
