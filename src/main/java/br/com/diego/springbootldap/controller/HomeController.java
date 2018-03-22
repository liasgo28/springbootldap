package br.com.diego.springbootldap.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.diego.springbootldap.Application;
import br.com.diego.springbootldap.model.Pessoa;
import br.com.diego.springbootldap.service.PessoaService;

@RestController
public class HomeController {
	@Autowired
    private PessoaService pessoaService;
	
	private static Logger log = LoggerFactory.getLogger(Application.class);
	
	@GetMapping("/")
    public String index() {
		log.info("Spring Boot + Spring LDAP Advanced LDAP Queries Example");

        List<Pessoa> names = pessoaService.getPessoaNamesByLastName("Diego");
        log.info("names: " + names);

        names = pessoaService.getPessoaNamesByLastName2("Bob");
        log.info("names: " + names);

        names = pessoaService.getPessoaNamesByLastName3("Fiorella");
        log.info("names: " + names);
		
		
		
        return "Bem vindo Diego!";
    }
	
	
}
