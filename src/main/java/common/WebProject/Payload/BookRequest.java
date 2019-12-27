/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.WebProject.Payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BookRequest {
     @Size(max = 140)
     @NotBlank
     @Valid
    private String Title;

   @NotBlank
   @Valid
   private String Content;

   @NotBlank
   @Valid
    private String Image;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title){
        this.Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}