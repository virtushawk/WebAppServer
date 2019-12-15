/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.WebProject.Service;

import common.WebProject.model.User;
import common.WebProject.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author romab
 */
@Service
public class UserService implements IUserService {
    
    @Autowired
    private UserRepository urepo;
    
    @Override
    public List<User> findAll()
    {
     
        return urepo.findAll();
    }
    
}
