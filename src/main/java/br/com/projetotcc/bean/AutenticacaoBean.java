package br.com.projetotcc.bean;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;

import br.com.projetotcc.dao.UsuarioDAO;
import br.com.projetotcc.domain.Pessoa;
import br.com.projetotcc.domain.Usuario;

@ManagedBean(name = "AutenticacaoMB")
@SessionScoped
public class AutenticacaoBean {
	private Usuario usuario;
	private Usuario usuarioLogado;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	@PostConstruct
	public void iniciar() {
		usuario = new Usuario();
		usuario.setPessoa(new Pessoa());
	}
	
	
	public void autenticar() {
		try {
			UsuarioDAO dao = new UsuarioDAO();
			usuarioLogado = dao.autenticar(usuario.getPessoa().getCpf(), usuario.getSenha());
			
			if(usuarioLogado == null) {
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Usuário ou senha incorretos."));
				return;
			}else {
				Faces.redirect("./dashboard.xhtml");
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Bem Vindo(a) à NEOFARM", usuarioLogado.getPessoa().getNome()+"."));
			}
			
			
			
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Não foi possível fazer o Login."));
		e.printStackTrace();
		}
	
	}
	
	public boolean temPermissoes(List<String> permissoes) {
		
		
		for (String permissao : permissoes) {
			if(usuarioLogado.getTipo() == permissao.charAt(0)) {
				return true;
			}
		}
		return false;
	}


	
	
}
