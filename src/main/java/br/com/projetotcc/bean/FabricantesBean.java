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
import org.primefaces.event.SelectEvent;
import com.sun.istack.logging.Logger;

import br.com.projetotcc.dao.FabricanteDAO;
import br.com.projetotcc.domain.Fabricante;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@ManagedBean(name = "FabricanteMB")
@ViewScoped
public class FabricantesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FabricantesBean.class);
	private Fabricante fabricante;
	private List<Fabricante> fabricantes;
	private String ultimoFabricanteNome;
	
	
	public String getUltimoFabricanteNome() {
		return ultimoFabricanteNome;
	}


	public void setUltimoFabricanteNome(String ultimoFabricanteNome) {
		this.ultimoFabricanteNome = ultimoFabricanteNome;
	}


	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		fabricante = new Fabricante();
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		fabricante.getId();
		fabricante.getDescricao();
		fabricante.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Fabricante> mostrarFabricantes() {

		log.info("Listando Fabricantes");
		try {
			FabricanteDAO dao = new FabricanteDAO();
			fabricantes = dao.listar();
			
			ultimoFabricanteNome = fabricantes.get(fabricantes.size()-1).getDescricao();
			

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return fabricantes;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			fabricante = (Fabricante) event.getComponent().getAttributes().get("fabricanteSelecionado");
			FabricanteDAO dao = new FabricanteDAO();
			dao.excluir(fabricante);
			fabricantes = dao.listar();
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
			
			FabricanteDAO dao = new FabricanteDAO();
			dao.merge(fabricante);
			novo();
			fabricantes = dao.listar();
			
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

			fabricante = (Fabricante) event.getComponent().getAttributes().get("fabricanteSelecionado");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));

	}

	// Metodo Selecionar
	public void onRowSelect(SelectEvent event) {

		FacesMessage msg = new FacesMessage("Item Selecionado: ", "( " + ((Fabricante) event.getObject()).getId()
				+ " ) " + ((Fabricante) event.getObject()).getDescricao());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}
	
	
	public void imprimir() throws Exception{
		 
        try {
            
        	String caminho = Faces.getRealPath("/reports/Fabricantes.jasper");

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
	


	public Fabricante getFabricante() {
		return fabricante;
	}


	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}


	public List<Fabricante> getFabricantes() {
		return fabricantes;
	}


	public void setFabricantes(List<Fabricante> fabricantes) {
		this.fabricantes = fabricantes;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static void setLog(Logger log) {
		FabricantesBean.log = log;
	}


	
	
 

}
