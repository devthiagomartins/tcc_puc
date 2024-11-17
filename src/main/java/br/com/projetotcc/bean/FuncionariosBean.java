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
import br.com.projetotcc.dao.FuncionarioDAO;
import br.com.projetotcc.domain.Pessoa;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import br.com.projetotcc.domain.Funcionario;
import br.com.projetotcc.domain.ModeloRelatorio;

@ManagedBean(name = "FuncionarioMB")
@ViewScoped
public class FuncionariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FuncionariosBean.class);
	private Funcionario funcionario;
	private List<Funcionario> funcionarios;
	private List<Pessoa> pessoas;

	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		
		try {
			funcionario = new Funcionario();
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

		funcionario.getId();
		funcionario.getPessoa().getNome();
		funcionario.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Funcionario> mostrarFuncionarios() {

		log.info("Listando Funcionários");
		try {
			FuncionarioDAO dao = new FuncionarioDAO();
			funcionarios = dao.listar("dataAdmissao");

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return funcionarios;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			funcionario = (Funcionario) event.getComponent().getAttributes().get("funcionarioSelecionado");
			FuncionarioDAO dao = new FuncionarioDAO();
			dao.excluir(funcionario);
			funcionarios = dao.listar();
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
			
			FuncionarioDAO dao = new FuncionarioDAO();
			dao.merge(funcionario);
			novo();
			
			PessoaDAO pessDAO = new PessoaDAO();
			pessoas = pessDAO.listar();
		
			
			funcionarios = dao.listar("dataAdmissao");
			
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
			funcionario = (Funcionario) event.getComponent().getAttributes().get("funcionarioSelecionado");
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
            
        	String caminho = Faces.getRealPath("/reports/Funcionarios.jasper");

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

	public Funcionario getFuncionario() {
		return funcionario;
	}


	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}


	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}


	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
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
		FuncionariosBean.log = log;
	}


	

}
