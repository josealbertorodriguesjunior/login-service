package com.juniorlima.login.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.juniorlima.login.model.LoginModel;
import com.juniorlima.login.model.PhoneModel;
import com.juniorlima.login.model.ProfileModel;
import com.juniorlima.login.model.StatusModel;
import com.juniorlima.login.repository.LoginRepository;
import com.juniorlima.login.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    LoginRepository loginRepository;

    Utils utils = new Utils();
    
    @GetMapping
    public String instructions(){
        return "Create Login --> domain.com:8080/api/sign-up<br/>"
                + "Login to account --> domain.com:8080/api/login<br/>"
                + "Update account --> domain.com:8080/api/update/user/{id}<br/>"
                + "Delete account --> domain.com:8080/api/delete/user/{id}<br/>";
    }

    @PostMapping("/sign-up")
    public StatusModel createLogin(@Valid @RequestBody LoginModel login) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Long verifyEmail = loginRepository.countByEmail(login.getEmail());
        List<PhoneModel> phones = new ArrayList<>();
        StatusModel status = new StatusModel();
        if (verifyEmail > 0) {
            System.out.println("This email is already in use");
            status.setMessage("E-mail já existente");
            return status;
        } else {
            ResponseEntity response = new ResponseEntity(HttpStatus.CREATED);
            login.setToken(utils.createHash(login.getEmail()));
            login.setPassword(utils.createHash(login.getPassword()));
            for (int i = 0; i < login.getPhones().size(); i++) {
                PhoneModel phone = new PhoneModel();
                phone.setDdd(login.getPhones().get(i).getDdd());
                phone.setPhone(login.getPhones().get(i).getPhone());
                phone.setLogin(login);
                phones.add(phone);
            }
            login.setPhones(phones);
            login.setCreatedAt(new Date());
            loginRepository.save(login);
            status.setStatus(response.toString());
            status.setMessage("Usuario criado com sucesso");
            status.setCreatedAt(login.getCreatedAt());
            status.setToken(login.getToken());
            System.out.println("User created");
        }
        return status;
    }

    @PutMapping("/update/user/{id}")
    public StatusModel updateLogin(@PathVariable(value = "id") Long id, @Valid @RequestBody LoginModel loginDetails) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StatusModel status = new StatusModel();
        LoginModel login = loginRepository.findUserById(id);
        login.setName(loginDetails.getName());
        login.setEmail(loginDetails.getEmail());
        login.setPassword(utils.createHash(loginDetails.getPassword()));
        login.setPhones(loginDetails.getPhones());
        login.setModifiedAt(new Date());
        login.setToken(utils.createHash(login.getEmail()));
        loginRepository.save(login);
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);
        status.setCreatedAt(login.getCreatedAt());
        status.setLastLogin(login.getLastLogin());
        status.setModifiedAt(login.getModifiedAt());
        status.setToken(login.getToken());
        status.setStatus(response.toString());
        status.setMessage("Usuário " + login.getEmail() + " atualizado com sucesso");
        return status;
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteLogin(@PathVariable(value = "id") Long id) {
        loginRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public StatusModel doLogin(@Valid @RequestBody LoginModel login) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StatusModel status = new StatusModel();
        login.setPassword(utils.createHash(login.getPassword()));
        String password = login.getPassword();
        String token = utils.createHash(login.getEmail());
        String email = login.getEmail();
        LoginModel loginTemp = loginRepository.findByToken(token);
        Long hasPassword = loginRepository.countByPassword(password);
        Long hasEmail = loginRepository.countByEmail(email);
        Long isValid = loginRepository.countByEmailAndPassword(email, password);
        if (isValid > 0) {
            loginTemp.setLastLogin(new Date());
            loginRepository.save(loginTemp);
            status.setToken(loginTemp.getToken());
            status.setCreatedAt(loginTemp.getCreatedAt());
            status.setModifiedAt(loginTemp.getModifiedAt());
            status.setLastLogin(loginTemp.getLastLogin());
            ResponseEntity response = new ResponseEntity(HttpStatus.OK);
            status.setStatus(response.toString());
            status.setMessage("Login bem sucedido");
        }
        if (hasEmail > 0 && hasPassword == 0) {
            ResponseEntity response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
            status.setStatus(response.toString());
            status.setStatus("Usuário e/ou senha inválidos");
        }
        if (hasEmail == 0) {
            ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
            status.setStatus(response.toString());
            status.setMessage("Usuário e/ou senha inválidos");
        }

        return status;
    }

    @GetMapping("/profile/user/{id}")
    public ProfileModel userProfile(@PathVariable(value = "id") Long id) {
        PhoneModel phone = new PhoneModel();
        List<PhoneModel> phones = new ArrayList<>();
        ProfileModel profile = new ProfileModel();
        LoginModel login = new LoginModel();
        login = loginRepository.findUserById(id);
        if (login == null) {
            ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
            profile.setMessage("No Users Found");
            profile.setStatus(response.toString());
            
        } else {
            long verify = utils.compareMinutes(new Date(), login.getLastLogin());
            if (login.getLastLogin() == null) {
                ResponseEntity response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
                profile.setMessage("Invalid Session! Please sign in to your account");
                profile.setStatus(response.toString());
            } else {
                if (verify <= 30) {
                    ResponseEntity response = new ResponseEntity(HttpStatus.OK);
                    profile.setEmail(login.getEmail());
                    profile.setName(login.getName());
                    profile.setPassword(login.getPassword());
                    for (int i = 0; i < login.getPhones().size(); i++) {
                        phone.setDdd(login.getPhones().get(i).getDdd());
                        phone.setPhone(login.getPhones().get(i).getPhone());
                        phones.add(phone);
                    }
                    profile.setPhones(phones);
                    profile.setStatus(response.toString());
                    profile.setMessage("Welcome " + profile.getName());
                } else {
                    ResponseEntity response = new ResponseEntity(HttpStatus.UNAUTHORIZED);
                    profile.setMessage("Invalid Session! Please sign in to your account");
                    profile.setStatus(response.toString());
                }
            }
        }

        return profile;
    }
    
}
