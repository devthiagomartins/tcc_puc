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

import org.omnifaces.util.Faces;

import com.sun.istack.logging.Logger;

import br.com.projetotcc.dao.PessoaDAO;
import br.com.projetotcc.dao.ClienteDAO;
import br.com.projetotcc.domain.Pessoa;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import br.com.projetotcc.domain.Cliente;
import br.com.projetotcc.domain.ModeloRelatorio;

@ManagedBean(name = "ClienteMB")
@ViewScoped
public class ClientesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ClientesBean.class);
	private Cliente cliente;
	private List<Cliente> clientes;
	private List<Pessoa> pessoas;
	private String ultimoClienteNome;
	


	public String getUltimoClienteNome() {
		return ultimoClienteNome;
	}


	public void setUltimoClienteNome(String ultimoClienteNome) {
		this.ultimoClienteNome = ultimoClienteNome;
	}


	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		
		try {
			cliente = new Cliente();
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");
		
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao gerar novo cliente."));
		e.printStackTrace();
			
		}
		
		
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		cliente.getId();
		cliente.getPessoa().getNome();
		cliente.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Cliente> mostrarClientes() {

		log.info("Listando Clientes");
		try {
			ClienteDAO dao = new ClienteDAO();
			clientes = dao.listar("dataCadastro");
		
			
			ultimoClienteNome = clientes.get(clientes.size()-1).getPessoa().getNome();

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return clientes;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			cliente = (Cliente) event.getComponent().getAttributes().get("clienteSelecionado");
			ClienteDAO dao = new ClienteDAO();
			dao.excluir(cliente);
			clientes = dao.listar();
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
			
			ClienteDAO dao = new ClienteDAO();
			dao.merge(cliente);
			novo();
			
			PessoaDAO pessDAO = new PessoaDAO();
			pessoas = pessDAO.listar();
		
			
			clientes = dao.listar("dataCadastro");
			
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
			cliente = (Cliente) event.getComponent().getAttributes().get("clienteSelecionado");
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
            
        	String caminho = Faces.getRealPath("/reports/Clientes.jasper");

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
	

	
	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public List<Cliente> getClientes() {
		return clientes;
	}


	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
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
		ClientesBean.log = log;
	}

	

}
