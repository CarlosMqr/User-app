package com.springboot.backend.carlos.usersapp.users_backed.controllers;

import com.springboot.backend.carlos.usersapp.users_backed.model.User;
import com.springboot.backend.carlos.usersapp.users_backed.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

     @GetMapping
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

     @PutMapping("/users/{id}")
    public ResponseEntity<User> update(@Valid @RequestBody User user, @PathVariable Long id, BindingResult result){
         Map<String, Object> response = new HashMap<>();
         Optional<User> userOptional = userServiceImpl.findById(id);
         User userDb = userOptional.get();
         User userUpdate;

         if (result.hasErrors()){
             List<String> errors = result.getFieldErrors()
                     .stream()
                     .map(err -> "El campo  " + err.getField() + "' " + err.getDefaultMessage() )
                     .collect(Collectors.toList());

             response.put("errors", errors);
             return new ResponseEntity(response,BAD_REQUEST);
     }

         if (userOptional.isEmpty()){
             response.put("Mensaje", "Error:, no se puede editar, el cliente con ID: ".concat(id.toString().concat(" no existe en la BD")));
             return new ResponseEntity(response,NOT_FOUND);
         }

         try {
             userDb.setName(user.getName());
             userDb.setLastname(user.getLastname());
             userDb.setEmail(user.getEmail());
             userDb.setUsername(user.getUsername());
             userDb.setPassword(user.getPassword());
             userUpdate = userServiceImpl.save(userDb);
             response.put("Mensaje", "El estudiante se actualizo con exito");
             response.put("Estudiante", userUpdate);
             return new ResponseEntity(response, OK);
         }catch (DataAccessException e){
             response.put("Mensaje", "Error al realizar el update");
             response.put("Error", e.getMostSpecificCause().getMessage());
             return new ResponseEntity(response, NOT_FOUND);
         }
     }


     @DeleteMapping("users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
     Map<String, Object> response =  new HashMap<>();
      Optional<User> userOptional = userServiceImpl.findById(id);

      if (userOptional.isEmpty()){
          response.put("Mensaje", "Error:, no se puede editar, el cliente con ID: ".concat(id.toString().concat(" no existe en la BD")));
          return new ResponseEntity(response,NOT_FOUND);
      }

      try {
           User user = userOptional.get();
           userServiceImpl.deleteById(user.getId());
          response.put("Mensaje", "El estudiante se elimino con exito");
          return  new ResponseEntity<>(response, OK);
      }catch (DataAccessException e){
          response.put("Mensaje", "Error al eliminar");
          response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
          return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
      }
     }


     @GetMapping("/user/name/{userName}")
    public ResponseEntity<?> findByUserName(@PathVariable String userName){
         Map<String, Object> response =  new HashMap<>();
         Optional<User> findUserName = userServiceImpl.findByUserName(userName);
        if (findUserName.isEmpty()){
            response.put("Mensaje", "Error: el UserName no encontrado");
            return new ResponseEntity(response,NOT_FOUND);
        }
        response.put("Mensaje", "El UserName se encontro con exito");
        response.put("UserName", findUserName.get());
        return new ResponseEntity(response, OK);
        }

}
