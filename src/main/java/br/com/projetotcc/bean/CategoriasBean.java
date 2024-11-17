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

import br.com.projetotcc.dao.CategoriaDAO;
import br.com.projetotcc.domain.Categoria;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@ManagedBean(name = "CategoriaMB")
@ViewScoped
public class CategoriasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CategoriasBean.class);
	private Categoria categoria;
	private List<Categoria> categorias;

	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		categoria = new Categoria();
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		categoria.getId();
		categoria.getDescricao();
		categoria.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Categoria> mostrarCategorias() {

		log.info("Listando Categorias");
		try {
			CategoriaDAO dao = new CategoriaDAO();
			categorias = dao.listar();

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return categorias;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			categoria = (Categoria) event.getComponent().getAttributes().get("categoriaSelecionada");
			CategoriaDAO dao = new CategoriaDAO();
			dao.excluir(categoria);
			categorias = dao.listar();
			
			
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
			
			CategoriaDAO dao = new CategoriaDAO();
			dao.merge(categoria);
			novo();
			categorias = dao.listar();
			
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

			categoria = (Categoria) event.getComponent().getAttributes().get("categoriaSelecionada");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));

	}

	// Metodo Selecionar
	public void onRowSelect(SelectEvent event) {

		FacesMessage msg = new FacesMessage("Item Selecionado: ", "( " + ((Categoria) event.getObject()).getId()
				+ " ) " + ((Categoria) event.getObject()).getDescricao());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}
	
	public void imprimir() throws Exception{
		 
        try {
            
        	String caminho = Faces.getRealPath("/reports/Categorias.jasper");

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
	


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}


	public List<Categoria> getCategorias() {
		return categorias;
	}


	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}



	public static void setLog(Logger log) {
		CategoriasBean.log = log;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
 

}
