package br.com.projetotcc.bean;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Faces;

import com.sun.istack.logging.Logger;
import br.com.projetotcc.dao.PessoaDAO;
import br.com.projetotcc.dao.UsuarioDAO;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.domain.Pessoa;
import br.com.projetotcc.domain.Usuario;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@ManagedBean(name = "UsuarioMB")
@ViewScoped
public class UsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UsuariosBean.class);
	private Usuario usuario;
	private List<Usuario> usuarios;
	private List<Pessoa> pessoas;

	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		
		try {
			usuario = new Usuario();
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");
		
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao gerar novo usuário."));
		e.printStackTrace();
			
		}
		
		
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		usuario.getId();
		usuario.getPessoa().getNome();
		usuario.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Usuario> mostrarUsuarios() {

		log.info("Listando Usuários");
		try {
			UsuarioDAO dao = new UsuarioDAO();
			usuarios = dao.listar("tipo");
			
			

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return usuarios;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			usuario = (Usuario) event.getComponent().getAttributes().get("usuarioSelecionado");
			UsuarioDAO dao = new UsuarioDAO();
			dao.excluir(usuario);
			usuarios = dao.listar();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Deletado com sucesso."));

		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao deletar"));
		e.printStackTrace();
		}

	}

	// Salvar
	public void salvar() {

		try {
			
			UsuarioDAO dao = new UsuarioDAO();
			
			SimpleHash hash = new SimpleHash("md5", usuario.getSenha());
			usuario.setSenha(hash.toHex());
			
			dao.merge(usuario);
			
			novo();
			
			PessoaDAO pessDAO = new PessoaDAO();
			pessoas = pessDAO.listar("nome");
			
			
			usuarios = dao.listar("tipo");
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));

		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao salvar"));
		e.printStackTrace();
		}

	}

	// Alterar
	public void alterar(ActionEvent event) {
		
		try {
			usuario = (Usuario) event.getComponent().getAttributes().get("usuarioSelecionado");
			PessoaDAO pessDAO = new PessoaDAO();
			pessoas = pessDAO.listar();
			
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao tentar selecionar um cliente."));
		e.printStackTrace();
			
		}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));
	}


	public void imprimir() throws Exception{
		 
        try {
            
        	String caminho = Faces.getRealPath("/reports/Usuarios.jasper");

			Map<String, Object> parametros = new HashMap<>();

			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			
			JasperPrintManager.printReport(relatorio, true);
            
        } 
        
        catch (JRException ex) {
            Logger.getLogger(ModeloRelatorio.class.getName(), null).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        } 
        
     
}   
	
	
	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public List<Usuario> getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


	public List<Pessoa> getPessoas() {
		return pessoas;
	}


	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static void setLog(Logger log) {
		UsuariosBean.log = log;
	}


	
	

}
