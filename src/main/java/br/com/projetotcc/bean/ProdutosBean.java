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
import br.com.projetotcc.dao.FabricanteDAO;
import br.com.projetotcc.dao.ProdutoDAO;
import br.com.projetotcc.domain.Categoria;
import br.com.projetotcc.domain.Fabricante;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.domain.Produto;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;


@ManagedBean(name = "ProdutoMB")
@ViewScoped
public class ProdutosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ProdutosBean.class);
	private Produto produto;
	private List<Produto> produtos;
	private List<Categoria> categorias;
	private List<Fabricante> fabricantes;
	private String ultimoProdutoNome;
	
	
	public String getUltimoProdutoNome() {
		return ultimoProdutoNome;
	}


	public void setUltimoProdutoNome(String ultimoProdutoNome) {
		this.ultimoProdutoNome = ultimoProdutoNome;
	}


	public static Logger getLog() {
		return log;
	}
	

	private void setMsg(String message) {
		// TODO Auto-generated method stub
	}
	
	public void novo() {
		
		try {
			produto = new Produto();
			CategoriaDAO categoriaDAO = new CategoriaDAO();
			categorias = categoriaDAO.listar("descricao");
			FabricanteDAO fabricanteDAO = new FabricanteDAO();
			fabricantes = fabricanteDAO.listar("descricao");
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao gerar nova categoria."));
		e.printStackTrace();
			
		}
		
		
	}

	public void limparformulario() {

		log.info("Dados Apagados!");

		produto.getId();
		produto.getDescricao();
		produto.getId();
	}

	//Listar
	@PostConstruct //é chamado logo apos o MB for criado , chama automaticamente o postConstruct
	public List<Produto> mostrarProdutos() {

		log.info("Listando Categorias");
		try {
			ProdutoDAO dao = new ProdutoDAO();
			produtos = dao.listar();
			
			ultimoProdutoNome = produtos.get(produtos.size()-1).getDescricao();
			
		} catch (RuntimeException e) {
			this.setMsg(e.getMessage());
			log.logSevereException(e);
		}
		return produtos;
		
		
	}

	// Excluir
	public void excluir(ActionEvent event) {

		try {
			
			produto = (Produto) event.getComponent().getAttributes().get("produtoSelecionado");
			ProdutoDAO dao = new ProdutoDAO();
			dao.excluir(produto);
			produtos = dao.listar();
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
			
			ProdutoDAO dao = new ProdutoDAO();
			dao.merge(produto);
			novo();
			
			CategoriaDAO catDAO = new CategoriaDAO();
			categorias = catDAO.listar();
			
			FabricanteDAO fabDAO = new FabricanteDAO();
			fabricantes = fabDAO.listar();
			
			produtos = dao.listar();
			
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
			produto = (Produto) event.getComponent().getAttributes().get("produtoSelecionado");
			CategoriaDAO categoriaDAO = new CategoriaDAO();
			categorias = categoriaDAO.listar();
			FabricanteDAO fabricanteDAO = new FabricanteDAO();
			fabricantes = fabricanteDAO.listar();
			

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));

		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao tentar selecionar um produto."));
		e.printStackTrace();
			
		}
		
			

	}

	// Metodo Selecionar
	public void onRowSelect(SelectEvent event) {

		FacesMessage msg = new FacesMessage("Item Selecionado: ", "( " + ((Produto) event.getObject()).getId()
				+ " ) " + ((Produto) event.getObject()).getDescricao());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}
	
	public void imprimir() throws Exception{
		 
	        try {
	            
	        	String caminho = Faces.getRealPath("/reports/Produtos.jasper");

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
		 
		 
		

	public Produto getProduto() {
		return produto;
	}


	public void setProduto(Produto produto) {
		this.produto = produto;
	}


	public List<Produto> getProdutos() {
		return produtos;
	}


	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static void setLog(Logger log) {
		ProdutosBean.log = log;
	}


	public List<Categoria> getCategorias() {
		return categorias;
	}


	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}


	public List<Fabricante> getFabricantes() {
		return fabricantes;
	}


	public void setFabricantes(List<Fabricante> fabricantes) {
		this.fabricantes = fabricantes;
	}

	

}
