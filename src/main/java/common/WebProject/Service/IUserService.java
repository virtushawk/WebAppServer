/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.WebProject.Service;

import common.WebProject.model.User;
import java.util.List;

/**
 *
 * @author romab
 */
public interface IUserService {
    
    List<User> findAll();
    
}
