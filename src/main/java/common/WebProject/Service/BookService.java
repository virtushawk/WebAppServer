/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.WebProject.Service;



import common.WebProject.Exception.BadRequestException;
import common.WebProject.Exception.ResourceNotFoundException;
import common.WebProject.Payload.BookRequest;
import common.WebProject.Payload.BookResponse;
import common.WebProject.Payload.PagedResponse;
import common.WebProject.Security.UserPrincipal;
import common.WebProject.model.Book;
import common.WebProject.model.User;
import common.WebProject.repository.BookRepository;
import common.WebProject.repository.UserRepository;
import common.WebProject.util.AppConstants;
import common.WebProject.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public PagedResponse<BookResponse> getAllBooks(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

  
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Book> books = bookRepository.findAll(pageable);

        if(books.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), books.getNumber(),
                    books.getSize(), books.getTotalElements(), books.getTotalPages(), books.isLast());
        }

        Map<Long, User> creatorMap = getPollCreatorMap(books.getContent());

        List<BookResponse> bookResponses = books.map(book -> {
            return ModelMapper.mapBookToBookResponse(book,
                    creatorMap.get(book.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(bookResponses, books.getNumber(),
                books.getSize(), books.getTotalElements(), books.getTotalPages(), books.isLast());
    }

    public PagedResponse<BookResponse> getBooksCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Book> books = bookRepository.findByCreatedBy(user.getId(), pageable);

        if (books.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), books.getNumber(),
                    books.getSize(), books.getTotalElements(), books.getTotalPages(), books.isLast());
        }

        List<BookResponse> bookResponses = books.map(book -> {
            return ModelMapper.mapBookToBookResponse(book,user);
        }).getContent();

        return new PagedResponse<>(bookResponses, books.getNumber(),
                books.getSize(), books.getTotalElements(), books.getTotalPages(), books.isLast());
    }

   


    public Book createBook(BookRequest bookRequest) {
        Book book = new Book();
        
        book.setTitle(bookRequest.getTitle());

        book.setContent(bookRequest.getContent());

        book.setImage(bookRequest.getImage());

        return bookRepository.save(book);
    }

    public BookResponse getBookById(Long bookId, UserPrincipal currentUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", bookId));

        // Retrieve Vote Counts of every choice belonging to the current poll

       
        // Retrieve poll creator details
        User creator = userRepository.findById(book.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", book.getCreatedBy()));


        return ModelMapper.mapBookToBookResponse(book,
                creator);
    }

  

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

   

    Map<Long, User> getPollCreatorMap(List<Book> books) {
   
        List<Long> creatorIds = books.stream()
                .map(Book::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}