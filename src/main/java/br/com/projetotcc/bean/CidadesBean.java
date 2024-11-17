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

import br.com.projetotcc.dao.EstadoDAO;
import br.com.projetotcc.dao.CidadeDAO;
import br.com.projetotcc.domain.Estado;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import br.com.projetotcc.domain.Cidade;

@ManagedBean(name = "CidadeMB")
@ViewScoped
public class CidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CidadesBean.class);
	private Cidade cidade;
	private List<Cidade> cidades;
	private List<Estado> estados;

	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		
		try {
			cidade = new Cidade();
			EstadoDAO categoriaDAO = new EstadoDAO();
			estados = categoriaDAO.listar("nome");
		
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao gerar novo estado."));
		e.printStackTrace();
			
		}
		
		
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		cidade.getId();
		cidade.getNome();
		cidade.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Cidade> mostrarCidades() {

		log.info("Listando Cidades");
		try {
			CidadeDAO dao = new CidadeDAO();
			cidades = dao.listar();

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return cidades;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			cidade = (Cidade) event.getComponent().getAttributes().get("cidadeSelecionada");
			CidadeDAO dao = new CidadeDAO();
			dao.excluir(cidade);
			cidades = dao.listar();
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
			
			CidadeDAO dao = new CidadeDAO();
			dao.merge(cidade);
			novo();
			
			EstadoDAO estDAO = new EstadoDAO();
			estados = estDAO.listar();
		
			
			cidades = dao.listar();
			
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
			cidade = (Cidade) event.getComponent().getAttributes().get("cidadeSelecionada");
			EstadoDAO estDAO = new EstadoDAO();
			estados = estDAO.listar();
			
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao tentar selecionar uma cidade."));
		e.printStackTrace();
			
		}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));
	}

	public void imprimir() throws Exception{
		 
        try {
            
        	String caminho = Faces.getRealPath("/reports/Cidades.jasper");

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

	public Cidade getCidade() {
		return cidade;
	}


	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}


	public List<Cidade> getCidades() {
		return cidades;
	}


	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}


	public List<Estado> getEstados() {
		return estados;
	}


	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static void setLog(Logger log) {
		CidadesBean.log = log;
	}


	
	

}
