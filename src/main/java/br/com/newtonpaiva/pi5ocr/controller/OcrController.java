package br.com.newtonpaiva.pi5ocr.controller;


import br.com.newtonpaiva.pi5ocr.service.AuthenticationToken;
import br.com.newtonpaiva.pi5ocr.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private AuthenticationToken authenticationToken;

    @PostMapping()
    public ResponseEntity<String> realizaOcr(@RequestParam(name="file") MultipartFile file, @RequestParam(name="username") String username, HttpServletRequest request) throws Exception{

        try {
            String token = authenticationToken.getBearer(request);
            if(!token.equals("")){
                authenticationToken.authenticate(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is not found!");
            }
            try {
                String response = ocrService.realizerOcr(file, username);
                if (response == null) {
                    return ResponseEntity.badRequest().body("Extensão não suportada, favor enviar um arquivo PNG ou JPG");
                } else {
                    return ResponseEntity.ok(response);
                }
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }


        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}