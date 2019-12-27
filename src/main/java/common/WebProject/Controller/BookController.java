/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.WebProject.Controller;


import common.WebProject.Payload.ApiResponse;
import common.WebProject.Payload.BookRequest;
import common.WebProject.Payload.BookResponse;
import common.WebProject.Payload.PagedResponse;
import common.WebProject.Security.CurrentUser;
import common.WebProject.Security.UserPrincipal;
import common.WebProject.Service.BookService;
import common.WebProject.model.Book;
import common.WebProject.repository.BookRepository;
import common.WebProject.repository.UserRepository;
import common.WebProject.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @GetMapping
    public PagedResponse<BookResponse> getBooks(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return bookService.getAllBooks(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookRequest bookRequest) {
        Book book = bookService.createBook(bookRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{bookId}")
                .buildAndExpand(book.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Book Created Successfully"));
    }

    @GetMapping("/{bookId}")
    public BookResponse getBookById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long bookId) {
        return bookService.getBookById(bookId, currentUser);
    }
}
