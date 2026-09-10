package main.java.com.programadoreschidos.abarroteria.kinal.service;

import main.java.com.programadoreschidos.abarroteria.kinal.dto.request.LoginDTORequest;
import main.java.com.programadoreschidos.abarroteria.kinal.dto.response.LoginDTOResponse;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.AuthRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.security.jbcrypt.BCrypt;

public class AuthService {
    
    //atributos
    private final AuthRepository authRepository;

    //constructor
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    //metodo
    public LoginDTOResponse login(LoginDTORequest loginDTORequest){
      if(loginDTORequest == null){
          throw new RuntimeException("los datos estan vacios");
      } else if (loginDTORequest.getEmail() == null ||loginDTORequest.getPassword() == null){
        throw new RuntimeException ("uno o los dos campos estan vacios");
      }else if(loginDTORequest.getEmail().isEmpty()||loginDTORequest.getPassword().isEmpty()) {
       throw new RuntimeException("No puedes dejar campos en blanco");
      }
      
    LoginDTOResponse response  = authRepository.findUserbyEmail(loginDTORequest);
    
    if(response == null){
        throw new RuntimeException("Usuario no encontrado");
    }
    
    
   if(response.getContrasenaHash()==null){
          throw new RuntimeException("no se ha podido concretar la operacion");
      }else{
    if (BCrypt.checkpw(loginDTORequest.getPassword(),response.getContrasenaHash())) {
        return response;         
        }
   }
    return null;
}
}
