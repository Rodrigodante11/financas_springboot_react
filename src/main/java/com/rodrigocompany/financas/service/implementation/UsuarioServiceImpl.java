package com.rodrigocompany.financas.service.implementation;

import com.rodrigocompany.financas.exception.ErroAUtenticacao;
import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.repository.UsuarioRepository;
import com.rodrigocompany.financas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    //@Autowired
    private UsuarioRepository repository;

    //@Autowired
    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {

        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isEmpty()){ // se volto vazio quer dizer que nao achou o email
            throw new ErroAUtenticacao("Usuario nao encontrado pelo email informado");

        }
        if(usuario.get().getSenha().equals(senha)) { // apos verificar o email faz a verificacao se a senha eh igual
            throw new ErroAUtenticacao(" Senha invalida");
        }

        return usuario;

    }

    @Override
    @Transactional  //aquela logica do faz tudo ou nao faz nada no BD
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);

    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe){
            throw new RegraNegocioException("Ja existe um usuario cadastrado com este email");
        }
    }
}
