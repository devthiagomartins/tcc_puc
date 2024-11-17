package br.com.projetotcc.bean;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
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
import br.com.projetotcc.dao.CidadeDAO;
import br.com.projetotcc.dao.EstadoDAO;
import br.com.projetotcc.dao.PessoaDAO;
import br.com.projetotcc.domain.Cidade;
import br.com.projetotcc.domain.Estado;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.domain.Pessoa;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@ManagedBean(name = "PessoaMB")
@ViewScoped
public class PessoasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PessoasBean.class);
	private Pessoa pessoa;
	private List<Pessoa> pessoas;
	private List<Cidade> cidades;
	private Estado estado;
	private List<Estado> estados;

	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		
		try {
			pessoa = new Pessoa();
			estado = new Estado();
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar("nome");
			
			cidades = new ArrayList<Cidade>();
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao gerar nova pessoa."));
		e.printStackTrace();
			
		}
		
		
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		pessoa.getId();
		pessoa.getNome();
		pessoa.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Pessoa> mostrarPessoas() {

		log.info("Listando Pessoas");
		try {
			PessoaDAO dao = new PessoaDAO();
			pessoas = dao.listar();

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return pessoas;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			pessoa = (Pessoa) event.getComponent().getAttributes().get("pessoaSelecionada");
			PessoaDAO dao = new PessoaDAO();
			dao.excluir(pessoa);
			pessoas = dao.listar();
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
			
			PessoaDAO dao = new PessoaDAO();
			dao.merge(pessoa);
			novo();
			estado = new Estado();
			
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();
			
			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.listar();
			
			pessoas = dao.listar();
			
			
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
			pessoa = (Pessoa) event.getComponent().getAttributes().get("pessoaSelecionada");
			
			estado = pessoa.getCidade().getEstado();
			
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();
			
			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.buscaPorEstado(estado.getId());
			
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao tentar selecionar uma pessoa."));
		e.printStackTrace();
			
		}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));
	}

	//selectOneMenu filtrando cada cidade por estado . 
	public void popular() {
		
		try {
			if(estado != null) { //o primeiro estado(registro) vale sempre null.
				CidadeDAO dao = new CidadeDAO();
				cidades = dao.buscaPorEstado(estado.getId());

				//System.out.println("Total de cidades"+cidades.size());
			}else {
				cidades = new ArrayList<>();
			}
		
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao tentar filtrar uma cidade."));
		e.printStackTrace();
		}
		
		
	}
	

	public void imprimir() throws Exception{
		 
        try {
            
        	String caminho = Faces.getRealPath("/reports/Pessoas.jasper");

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


	public Pessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}


	public List<Pessoa> getPessoas() {
		return pessoas;
	}


	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}


	public List<Cidade> getCidades() {
		return cidades;
	}


	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static void setLog(Logger log) {
		PessoasBean.log = log;
	}


	public List<Estado> getEstados() {
		return estados;
	}


	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}


	public Estado getEstado() {
		return estado;
	}


	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	
	
}
