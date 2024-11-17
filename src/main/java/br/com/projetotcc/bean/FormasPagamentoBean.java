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

import br.com.projetotcc.dao.FormaPagamentoDAO;
import br.com.projetotcc.domain.FormaPagamento;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@ManagedBean(name = "FormaPagamentoMB")
@ViewScoped
public class FormasPagamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FormasPagamentoBean.class);
	private FormaPagamento formaPagamento;
	private List<FormaPagamento> formaPagamentos;

	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		formaPagamento = new FormaPagamento();
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		formaPagamento.getId();
		formaPagamento.getDescricao();
		formaPagamento.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<FormaPagamento> mostrarFormaPagamentos() {

		log.info("Listando Formas de Pagamento");
		try {
			FormaPagamentoDAO dao = new FormaPagamentoDAO();
			formaPagamentos = dao.listar();

		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return formaPagamentos;
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			formaPagamento = (FormaPagamento) event.getComponent().getAttributes().get("formaPagamentoSelecionada");
			FormaPagamentoDAO dao = new FormaPagamentoDAO();
			dao.excluir(formaPagamento);
			formaPagamentos = dao.listar();
			
			
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
			
			FormaPagamentoDAO dao = new FormaPagamentoDAO();
			dao.merge(formaPagamento);
			novo();
			formaPagamentos = dao.listar();
			
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

			formaPagamento = (FormaPagamento) event.getComponent().getAttributes().get("formaPagamentoSelecionada");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));

	}

	// Metodo Selecionar
	public void onRowSelect(SelectEvent event) {

		FacesMessage msg = new FacesMessage("Item Selecionado: ", "( " + ((FormaPagamento) event.getObject()).getId()
				+ " ) " + ((FormaPagamento) event.getObject()).getDescricao());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	
	public void imprimir() throws Exception{
		 
        try {
            
        	String caminho = Faces.getRealPath("/reports/FormasPagamento.jasper");

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

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}


	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}


	public List<FormaPagamento> getFormaPagamentos() {
		return formaPagamentos;
	}


	public void setFormaPagamentos(List<FormaPagamento> formaPagamentos) {
		this.formaPagamentos = formaPagamentos;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static void setLog(Logger log) {
		FormasPagamentoBean.log = log;
	}


	
	
	
	
	
 

}
