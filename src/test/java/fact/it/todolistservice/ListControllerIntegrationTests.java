package fact.it.todolistservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.todolistservice.model.TodoList;
import fact.it.todolistservice.repository.ListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import fact.it.todolistservice.model.TodoList;
import fact.it.todolistservice.repository.ListRepository;

import javax.print.attribute.standard.Media;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.awt.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ListControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListRepository listRepository;

    private TodoList list1 = new TodoList("List1","Category1");
    private TodoList list2 = new TodoList("List2","Category2");

    @BeforeEach
    public void beforeAllTests() {
        listRepository.deleteAll();
        listRepository.save(list1);
        listRepository.save(list2);
    }

    @AfterEach
    public void afterAllTests(){
        listRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenTodoList_whenGetTodoListByNaam_thenReturnJsonTodoList() throws Exception {
        mockMvc.perform(get("/lists/naam/{naam}", "List1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam",is("List1")))
                .andExpect(jsonPath("$.category",is("Category1")));
    }

    @Test
    public void givenTodoLists_whenGetTodoListByCateogry_thenReturnJsonTodoLists() throws Exception{
        mockMvc.perform(get("/lists/category/{category}","Category"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].naam",is("List1")))
                .andExpect(jsonPath("$[0].category",is("Category1")))
                .andExpect(jsonPath("$[1].naam",is("List2")))
                .andExpect(jsonPath("$[1].category",is("Category2")));
    }

//    @Test
//    public void whenPostTodoList_thenReturnJsonTodoList() throws Exception {
//        TodoList list1 = new TodoList("List1","Category1");
//
//        mockMvc.perform(post("/lists")
//                .content(mapper.writeValueAsString(list1))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.naam", is("List1")))
//                .andExpect(jsonPath("$.category",is("Category1")));
//
//    }

}
