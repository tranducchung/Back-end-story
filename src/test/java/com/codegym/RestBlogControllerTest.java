package com.codegym;


import com.codegym.controller.RestBlogController;
import com.codegym.model.Blog;
import com.codegym.model.User;
import com.codegym.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebAppConfiguration
public class RestBlogControllerTest {
    private static List<Blog> blogList;
    private static Date DATE = Calendar.getInstance().getTime();
    private static String PATTERN = "MM/dd/yyyy HH:mm:ss";
    private static DateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN);
    private static String DATE_STRING = DATE_FORMAT.format(DATE);

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



    @BeforeClass
    public static void setupTestData() {
        //String content, String title, String createDate, User user, String hashTags
        blogList = new ArrayList<Blog>();
        User user = new User("Thanh", "NbThanh", "qweqe@gmail.com", "123123123");

        blogList.add(new Blog(1L, "Helo world", "hello", DATE_STRING, user, "#A#A#A#A#A#A#A#"));
        blogList.add(new Blog(2L, "Helo world", "hello", DATE_STRING, user, "#A#A#A#A#A#A#A#"));
        blogList.add(new Blog(3L, "Helo world", "hello", DATE_STRING, user, "#A#A#A#A#A#A#A#"));
        blogList.add(new Blog(4L, "Helo world", "hello", DATE_STRING, user, "#A#A#A#A#A#A#A#"));
        blogList.add(new Blog(5L, "Helo world", "hello", DATE_STRING, user, "#A#A#A#A#A#A#A#"));
        blogList.add(new Blog(6L, "Helo world", "hello", DATE_STRING, user, "#A#A#A#A#A#A#A#"));
    }

    // test get all blog

    @Test
    public void test_getAll_success() throws Exception {
        when(blogService.findAll()).thenReturn(blogList);

        mockMvc.perform(get("/api/blogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(6)));
    }

    // test get blog by id

    @Test
    public void test_getBlogBy_id() throws Exception {
        User user = new User("Thanh", "NbThanh", "qweqe@gmail.com", "123123123");
        Blog blog = new Blog(1l, "Hello", "World", DATE_STRING, user, "####A#A#A#A#A#A");
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
        Blog blog = new Blog(6l, "Hello", "World", null, user, "#A#A#A#A#A#A#a");
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
