package com.codegym;


import com.codegym.controller.RestBlogController;
import com.codegym.model.Blog;
import com.codegym.model.User;
import com.codegym.repository.BlogRepository;
import com.codegym.service.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebAppConfiguration
public class RestBlogControllerTest {

    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();

    @Mock
    private BlogService blogService; // gia lap tang service

    @InjectMocks
    private RestBlogController restBlogController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restBlogController).addFilters(new CORSFilter()).build();
    }

    private static List<Blog> blogList;

    @BeforeClass
    public static void setupTestData() {
        blogList = new ArrayList<Blog>();
        User user = new User("Thanh", "NbThanh", "qweqe@gmail.com", "123123123");
        Date date = Calendar.getInstance().getTime();
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String strDate = dateFormat.format(date);
        blogList.add(new Blog(1L, "Helo world", "hello", strDate, user));
        blogList.add(new Blog(2L, "Helo world", "hello", strDate, user));
        blogList.add(new Blog(3L, "Helo world", "hello", strDate, user));
        blogList.add(new Blog(4L, "Helo world", "hello", strDate, user));
        blogList.add(new Blog(5L, "Helo world", "hello", strDate, user));
    }

    // test get all blog

    @Test
    public void test_getAll_success() throws Exception {
        when(blogService.findAll()).thenReturn(blogList);

        mockMvc.perform(get("/api/blogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    // test get blog by id

    @Test
    public void test_getBlogBy_id() throws Exception {
        User user = new User("Thanh", "NbThanh", "qweqe@gmail.com", "123123123");
        Date date = Calendar.getInstance().getTime();
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String strDate = dateFormat.format(date);
        Blog blog = new Blog(1l, "Hello", "World", strDate, user);
        when(blogService.findById((long) 1)).thenReturn(blog);
        mockMvc.perform(get("/api/blogs/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is(blog.getContent())));
    }


    // test create blog

//    @Test
//    public void test_createBlog_success() throws Exception {
//        User user = new User("Thaweqrnh", "NbTwerhaqnh", "qweqe@qwergmail.com", "1q23wer123123");
//        Date date = Calendar.getInstance().getTime();
//        String pattern = "MM/dd/yyyy HH:mm:ss";
//        DateFormat dateFormat = new SimpleDateFormat(pattern);
//        String strDate = dateFormat.format(date);
//        Blog blog = new Blog(6L, "aasdas", "asdassd", strDate, user);
//        doNothing().when(blogService).save(blog);
//        mockMvc.perform(post("/api/blogs/create")
//                .content(om.writeValueAsString(blog))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//    }

    @Test
    public void test_update_user_success() throws Exception {
        User user = new User("Thanh", "NbThanh", "qweqe@gmail.com", "123123123");
        Blog blog = new Blog(6l, "Hello", "World", null, user);
        when(blogService.findById(blog.getId())).thenReturn(blog);
        doNothing().when(blogService).save(blog);
        mockMvc.perform(
                put("/api/blogs/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(blog)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_deleteBlogById_success() throws Exception {

        when(blogService.findById(blogList.get(1).getId())).thenReturn(blogList.get(1));
        doNothing().when(blogService).remote(blogList.get(1));
        mockMvc.perform(
                delete("/api/blogs/{id}", blogList.get(1).getId()))
                .andExpect(status().isOk());
    }

}