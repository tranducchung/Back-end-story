package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Tags;
import com.codegym.model.User;
import com.codegym.payload.response.PagedResponse;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogService;
import com.codegym.service.TagService;
import com.codegym.service.UserService;
import com.codegym.until.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestBlogController {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    // get all blog

    @GetMapping("/api/blogs")
    public ResponseEntity<List<Blog>> getAllBlog() {
        List<Blog> listBlog = (List<Blog>) blogService.findAll();
        if (listBlog.isEmpty()) {
            return new ResponseEntity<List<Blog>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
    }

    // get 1 blog

    @GetMapping("/api/blogs/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable("id") Long id) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }

    //get 1 custom blog of custom user

    @RequestMapping(value = {"/api/users/{userId}/blogs/{blogId}"}, method = RequestMethod.GET)
    public ResponseEntity<Blog> getCustomBlog(@PathVariable("userId") Long id, @PathVariable("blogId") Long blogId) {
        User user = userService.findUserByID(id);
        Blog blog = blogService.findByIdAndUser(blogId, user);
        if (blog == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }

    // create blog

    @PostMapping("/api/blogs/create")
    public ResponseEntity<Void> createBlog(@RequestBody Blog blog, HttpServletRequest request) {
        List<String> newTags = new ArrayList<>();
        List<Tags> tagsList = new ArrayList<>();
        // get user from token s
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userID = ((UserPrinciple) principal).getId();
        User user = userService.findUserByID(userID);
        blog.setUser(user);
        String[] listTags = convertStringToArray(blog.getHashTags());
        saveTagToDatabase(listTags, newTags);
        addTagsToBlogModel(tagsList, newTags);
        blog.setTags(tagsList);
        blogService.save(blog);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
    }

    // delete blog
    @RequestMapping(value = {"/api/blogs/{id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBlog(@PathVariable("id") Long id) {
        Blog blog = blogService.findById(id);
        if (blog != null) {
            blogService.remote(blog);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }


    // edit blog

    @RequestMapping(value = {"/api/blogs/{id}"}, method = RequestMethod.PUT)
    public ResponseEntity<Blog> editBlog(@RequestBody Blog blog, @PathVariable("id") Long id) {
        List<String> newTags = new ArrayList<>();
        List<Tags> tagsList = new ArrayList<>();

        Blog blogInDB = blogService.findById(id);
        if (blogInDB == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        blogInDB.setTitle(blog.getTitle());
        blogInDB.setContent(blog.getContent());
        blogInDB.setUrlVideo(blog.getUrlVideo());
        blogInDB.setHashTags(blog.getHashTags());
        String[] listTags = convertStringToArray(blog.getHashTags());
        saveTagToDatabase(listTags, newTags);
        addTagsToBlogModel(tagsList, newTags);
        blogInDB.setTags(tagsList);
        blogService.save(blogInDB);
        return new ResponseEntity<Blog>(blogInDB, HttpStatus.OK);
    }

    //get all blog in database by id and DESC

    @RequestMapping(value = {"/api/blogs-getall"}, method = RequestMethod.GET)
    public ResponseEntity<List<Blog>> getAllBlogSortedByIdDESC() {
        List<Blog> listBlog = blogService.findAllBlogByIdOderById();
        if( listBlog.isEmpty()) {
            return new ResponseEntity<List<Blog>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
    }


    // get alll blog in database by id blog sorted DESC when user_id = ?

    @RequestMapping(value = {"/api/blogs/getall"}, method = RequestMethod.GET)
    public ResponseEntity<List<Blog>> getAllBlogByUserIdAndSortBlogIdDESC() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((UserPrinciple) authen).getId();
        List<Blog> listBlog = blogService.findAllByUserId(userId);
        if (listBlog.isEmpty()) {
            return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
    }


    // find all blog by title
    @GetMapping("/api/blogs/searchAll")
    public ResponseEntity<List<Blog>> findAllBlogByTitleContaining(Optional<String> title) {
        List<Blog> listBlog;
        if (title.isPresent()) {
            listBlog = blogService.findAllByTitleContaining(title.get());
            return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
        }
        listBlog = blogService.findAll();
        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
    }

    @GetMapping("/api/blogs/user/searchall")
    public ResponseEntity<List<Blog>> findAllBlogByTitleOfUser(@RequestParam("title") Optional<String> title) {
        // get user from token
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = ((UserPrinciple) authen).getId();
        User user = userService.findUserByID(user_id);
        List<Blog> listBlog;
        if (title.isPresent() && user != null) {
            listBlog = blogService.findAllByTitleContainingAndUser(title.get(), user);
            if (listBlog.isEmpty()) {
                return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
        }
        listBlog = blogService.findAllByUserId(user_id);
        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
    }

    // find all blog by tag

    @GetMapping("/api/blogs/hashtag/{hashtag}")
    public ResponseEntity<List<Blog>> findAllBlogByHashTag(@PathVariable("hashtag") String hashtag) {
        Tags tags = tagService.findByName(hashtag);
        if (tags == null) {
            System.out.println("j");
            return new ResponseEntity<List<Blog>>(HttpStatus.NOT_FOUND);
        }
        List<Blog> blogList = blogService.findByTags(tags);
        if (blogList.isEmpty()) {
            return new ResponseEntity<List<Blog>>(blogList, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Blog>>(blogList, HttpStatus.OK);
    }

    @GetMapping("/api/blogs/users/{userId}")
    public ResponseEntity<?> getNotesOfUser(@PathVariable("userId") Long userId,
                                            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        PagedResponse<Blog> blogs = blogService.getBlogsByUserId(userId, page, size);
        return new ResponseEntity<PagedResponse<Blog>>(blogs, HttpStatus.OK);
    }

    private String[] convertStringToArray(String hashtag) {
        return hashtag.split("#");
    }


    // add tag to tag table

    private void addTagsToTagsTable(Tags tags) {
        if (!tagService.existsByName(tags.getName())) {
            tagService.save(tags);
        }
    }

    private void saveTagToDatabase(String[] listTags, List<String> newTags) {
        for (int i = 0; i < listTags.length; i++) {
            if (!newTags.contains(listTags[i])) {
                newTags.add(listTags[i]);
                if (!tagService.existsByName(listTags[i])) {
                    Tags tags = new Tags();
                    tags.setName(listTags[i]);
                    tagService.save(tags);
                }
            }
        }
    }

    private void addTagsToBlogModel(List<Tags> tagsList, List<String> newTags) {
        for (int i = 0; i < newTags.size(); i++) {
            Tags tags = tagService.findByName(newTags.get(i));
            if (tags != null) {
                tagsList.add(tags);
            }
        }
    }

}










