/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.WebProject.util;



import common.WebProject.Payload.BookResponse;
import common.WebProject.Payload.UserSummary;
import common.WebProject.model.Book;
import common.WebProject.model.User;

public class ModelMapper {

   
    
     public static BookResponse mapBookToBookResponse(Book book, User creator) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setCreationDateTime(book.getCreatedAt());
        bookResponse.setImage(book.getImage());
        bookResponse.setContent(book.getContent());

        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
       bookResponse.setCreatedBy(creatorSummary);

        return bookResponse;
    }
}