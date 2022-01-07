package fact.it.todolistservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.todolistservice.model.TodoList;
import fact.it.todolistservice.repository.ListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ListControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListRepository listRepository;

    private TodoList todoList1 = new TodoList("List1", "Category1");
    private TodoList todoList2 = new TodoList("List2", "Category2");

    private List<TodoList> allTodoLists = Arrays.asList(todoList1, todoList2);

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenTodoList_whenGetTodoListByNaam_thenReturnJsonTodoList() throws Exception{
        given(listRepository.findTodoListByNaam("List1")).willReturn(todoList1);

        mockMvc.perform(get("/lists/naam/{naam}","List1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam",is("List1")))
                .andExpect(jsonPath("$.category",is("Category1")));
    }

    @Test
    public void givenTodoLists_whenGetTodoListsByCategory_thenReturnJsonTodoLists() throws Exception {
        given(listRepository.findListsByCategoryContaining("Category")).willReturn(allTodoLists);

        mockMvc.perform(get("/lists/category/{category}", "Category"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].naam",is("List1")))
                .andExpect(jsonPath("$[0].category",is("Category1")))
                .andExpect(jsonPath("$[1].naam",is("List2")))
                .andExpect(jsonPath("$[1].category",is("Category2")));
    }

    @Test
    public void whenPostTodoList_thenReturnJsonTodoList() throws Exception{
        TodoList todoList3 = new TodoList("List3","Category3");

        mockMvc.perform(post("/lists")
                .content(mapper.writeValueAsString(todoList3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam",is("List3")))
                .andExpect(jsonPath("$.category",is("Category3")));
    }

    @Test
    public void givenTodoList_whenPutTodoList_thenReturnJsonTodoList() throws Exception{
        TodoList todoList1 = new TodoList("List1","Category1");

        given(listRepository.findTodoListByNaam("List1")).willReturn(todoList1);

        TodoList updatedList = new TodoList("List1","Category2");

        mockMvc.perform(put("/lists")
                        .content(mapper.writeValueAsString(updatedList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam",is("List1")))
                .andExpect(jsonPath("$.category",is("Category2")));
    }

    @Test
    public void givenTodoList_whenDeleteTodoList_thenStatusOk() throws Exception{
        TodoList listToBeDeleted = new TodoList("List9","Category9");

        given(listRepository.findTodoListByNaam("List9")).willReturn(listToBeDeleted);

        mockMvc.perform(delete("/lists/naam/{naam}","List9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoTodoList_whenDeleteTodoList_thenStatusNotFound() throws Exception{
        given(listRepository.findTodoListByNaam("List8")).willReturn(null);

        mockMvc.perform(delete("/lists/naam/{naam}","List8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
