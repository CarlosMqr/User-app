package com.springboot.backend.carlos.usersapp.users_backed.controllers;

import com.springboot.backend.carlos.usersapp.users_backed.model.User;
import com.springboot.backend.carlos.usersapp.users_backed.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})// para poder enviar y recibir datos
@RestController// indicar que es una clase restcontroller
@RequestMapping("/api")// aqui se genera la u
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

     @GetMapping("/users")
    public List<User> list(){
         return userServiceImpl.findAll();
     }


     @GetMapping("/users/{id}")
    public ResponseEntity<User> show(@PathVariable Long id){
         Map<String, Object> response = new HashMap<>();
         Optional<User> userOptional;

         try {
              userOptional = userServiceImpl.findById(id);
         }catch (DataAccessException e){
             response.put("Mensaje", "Error al realizar la consulta en la base de datos");
             response.put("error", e.getCause().getMessage());
             return new ResponseEntity(response,INTERNAL_SERVER_ERROR);
         }

         if (userOptional.isPresent()){
             return ResponseEntity.ok(userOptional.orElseThrow());
         }else {
             response.put("Mensaje", "el cliente con ID: ".concat(id.toString().concat(" no existe en la BD")));
           return new ResponseEntity(response, NOT_FOUND);
         }
     }

     @PostMapping("/users")
    public ResponseEntity<User> create(@Valid @RequestBody User user, BindingResult result){
     Map<String, Object> response = new HashMap<>();
      User userC;

      if (result.hasErrors()){
          List<String> errors = result.getFieldErrors()
                  .stream()
                  .map(err -> "El campo  " + err.getField() + "' " + err.getDefaultMessage() )
                  .collect(Collectors.toList());
          response.put("errors", errors);
          return new ResponseEntity(response,BAD_REQUEST);
      }

      try {
          userC = userServiceImpl.save(user);
      }catch (DataAccessException e){
          response.put("Mensaje", "Error al realizar el INSERT en la BD");
          response.put("Error", e.getCause().getMessage());
          return  new ResponseEntity(response, INTERNAL_SERVER_ERROR);
      }
         response.put("Mensaje", "El usuario ha sido creado con éxito");
         response.put("Usuario", userC);
         return new ResponseEntity(response,CREATED);
     }



}
