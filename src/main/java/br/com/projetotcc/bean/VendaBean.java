package br.com.projetotcc.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.mail.EmailException;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import com.sun.istack.logging.Logger;

import br.com.projetotcc.dao.ClienteDAO;
import br.com.projetotcc.dao.FormaPagamentoDAO;
import br.com.projetotcc.dao.FuncionarioDAO;
import br.com.projetotcc.dao.ProdutoDAO;
import br.com.projetotcc.dao.VendaDAO;
import br.com.projetotcc.domain.Cliente;
import br.com.projetotcc.domain.FormaPagamento;
import br.com.projetotcc.domain.Funcionario;
import br.com.projetotcc.domain.ItemVenda;
import br.com.projetotcc.domain.ModeloRelatorio;
import br.com.projetotcc.domain.Produto;
import br.com.projetotcc.domain.SendMail;
import br.com.projetotcc.domain.SoundMessages;
import br.com.projetotcc.domain.Venda;
import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@SuppressWarnings("serial")
@ManagedBean(name = "VendaMB")
@ViewScoped
public class VendaBean implements Serializable {
	private List<Produto> produtos;
	private Venda venda;
	private List<Venda> vendas;
	private List<ItemVenda> itensVenda;
	private List<Cliente> clientes;
	private Cliente cliente;
	private List<Funcionario> funcionarios;
	private List<FormaPagamento> formaPagamentos;
	private String ulimaVendaCliente;
	private Date ultimaVendaDataHora;
	private Integer progress1;
    private Integer progress2;
    private String emailClienteVenda;
	

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Date getUltimaVendaDataHora() {
		return ultimaVendaDataHora;
	}

	public void setUltimaVendaDataHora(Date ultimaVendaDataHora) {
		this.ultimaVendaDataHora = ultimaVendaDataHora;
	}

	public String getUlimaVendaCliente() {
		return ulimaVendaCliente;
	}

	public void setUlimaVendaCliente(String ulimaVendaCliente) {
		this.ulimaVendaCliente = ulimaVendaCliente;
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}
	
	 public Integer getProgress1() {
	        progress1 = updateProgress(progress1);
	        return progress1;
	    }
	 
	    public Integer getProgress2() {
	        progress2 = updateProgress(progress2);
	        return progress2;
	    }

	    
	    
	public List<Venda> mostrarVendas() {

		try {
			VendaDAO dao = new VendaDAO();
			vendas = dao.listar("horario");
			
			ulimaVendaCliente = vendas.get(vendas.size() - 1).getCliente().getPessoa().getNome();
			ultimaVendaDataHora = vendas.get(vendas.size() - 1).getHorario();
			System.out.println(ultimaVendaDataHora);

		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return vendas;
	}
	
	public String horaUltimaVenda() {
		VendaDAO dao = new VendaDAO();
		vendas = dao.listar();
		ultimaVendaDataHora = vendas.get(vendas.size() - 1).getHorario();
		return ultimaVendaDataHora.toString();
	}

	@PostConstruct
	public void novo() {
		try {
			venda = new Venda();
			venda.setValorTotal(new BigDecimal("0.00"));

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar("descricao");

			itensVenda = new ArrayList<>();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar carregar a tela de vendas");
			erro.printStackTrace();
		}
	}

	public void adicionar(ActionEvent evento) {
		Produto produto = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");

		int achou = -1; // nao achou, se positivo achou.

		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			if (itensVenda.get(posicao).getProduto().equals(produto)) { // capturar item na linha corrente
				achou = posicao;
			}
		}

		if (achou < 0) { // é um item novo pois nao achou nenhum registro .
			ItemVenda itemVenda = new ItemVenda();
			itemVenda.setPrecoParcial(produto.getPreco());
			itemVenda.setProduto(produto);
			itemVenda.setQuantidade(new Short("1"));
			itensVenda.add(itemVenda);

		} else { // editar //qtde é Short, ao somar 1 , vai ser convertido para int , entao tem q
					// transformar novamente pra short
			ItemVenda itemVenda = itensVenda.get(achou);
			itemVenda.setQuantidade(new Short(itemVenda.getQuantidade() + 1 + ""));
			itemVenda.setPrecoParcial(produto.getPreco().multiply(new BigDecimal(itemVenda.getQuantidade())));
		}

		calcular();

	}

	public void remover(ActionEvent event) {
		ItemVenda itemVenda = (ItemVenda) event.getComponent().getAttributes().get("itemSelecionado");

		int achou = -1;

		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {

			if (itensVenda.get(posicao).getProduto().equals(itemVenda.getProduto())) {
				achou = posicao;
			}
		}

		if (achou > -1) { // foi encontrado , entao pode remover
			itensVenda.remove(achou);
		}

		calcular();
	}

	public void calcular() {

		venda.setValorTotal(new BigDecimal("0.00"));

		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			ItemVenda itemVenda = itensVenda.get(posicao);
			venda.setValorTotal(venda.getValorTotal().add(itemVenda.getPrecoParcial())); // preco total antigo + preco
																							// parcial do item corrente
																							// e atualiza.
		}
	}
	

	public void finalizar() {
		try {

			venda.setHorario(new Date());

			FuncionarioDAO funcDAO = new FuncionarioDAO();
			funcionarios = funcDAO.listar();

			ClienteDAO cliDAO = new ClienteDAO();
			clientes = cliDAO.listar();

			FormaPagamentoDAO pgtoDAO = new FormaPagamentoDAO();
			formaPagamentos = pgtoDAO.listar();

		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao finalizar a venda."));
			e.printStackTrace();
		}
	}

	public void salvar() {

		try {
			if (venda.getValorTotal().signum() == 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
						"Informe pelo menos um item para a venda."));
				return;
			}else if (venda.getFuncionario().getAtivo() == false) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
						"O funcionário deve estar ATIVO para realizar a venda."));
				return;
			}

			VendaDAO vendaDAO = new VendaDAO();
			vendaDAO.save(venda, itensVenda);

			novo();
			
			SoundMessages sound = new SoundMessages();
			sound.executaSom("D:\\vendaRealizada.wav");
			
			atualizaNotificacaoVenda();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Salvo com sucesso."));
			
			
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao salvar"));
			e.printStackTrace();
		} 
	}

	public void sound() {
		
		SoundMessages sound = new SoundMessages();
		sound.executaSom("D:\\vendaRealizada.wav");
	}
	
	public void sendMail(){
		
		List<Venda> vendas = null;
		
		VendaDAO dao = new VendaDAO();
		vendas = dao.listar();
		
		String clienteDaVenda = vendas.get(vendas.size() - 1).getCliente().getPessoa().getEmail();
		String nomeCliente = vendas.get(vendas.size() - 1).getCliente().getPessoa().getNome();
		
		
		try {
			SendMail email = new SendMail();
			if(clienteDaVenda == null || clienteDaVenda == "") {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Email do cliente não informado."));
			}else {
				email.sendMail(clienteDaVenda,nomeCliente);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Email enviado com sucesso."));
				
			}
			
			
			
		} catch (EmailException e) {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Erro ao enviar email."));
			e.printStackTrace();
			
		}
		
		
		
	}
	
	public void atualizaNotificacaoVenda() {
		
			PrimeFaces.current().ajax().update(":formBar:notificacaoVenda");
	}
	
	 public void onComplete() {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email enviado com sucesso."));
	    }
	
	 
	 public void cancel() {
	        progress1 = null;
	        progress2 = null;
	    }
	
	 
	 
	 private Integer updateProgress(Integer progress) {
	        if(progress == null) {
	            progress = 0;
	        }
	        else {
	            progress = progress + (int)(Math.random() * 16);
	             
	            if(progress > 100)
	                progress = 100;
	        }
	         
	        return progress;
	    }
	 
	 
	 
	 
		public void imprimir() throws Exception{
			 
	        try {
	            
	        	String caminho = Faces.getRealPath("/reports/Vendas.jasper");

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
		
		public String vTotalVendas() {
			VendaDAO vendaDAO = new VendaDAO();
			String vTotal = vendaDAO.valorTotalVendas();
			return vTotal;
		}
		
		public String qtdeVendasDiaria() {
			VendaDAO vendaDAO = new VendaDAO();
			String qtde = vendaDAO.vendasDiaria();
			return qtde;
		}
		
		public String emailCliente() {
			emailClienteVenda =  vendas.get(vendas.size() - 1).getCliente().getPessoa().getEmail();
			return emailClienteVenda;
		}
	 
	 
	public String getEmailClienteVenda() {
			return emailClienteVenda;
		}

		public void setEmailClienteVenda(String emailClienteVenda) {
			this.emailClienteVenda = emailClienteVenda;
		}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<FormaPagamento> getFormaPagamentos() {
		return formaPagamentos;
	}

	public void setFormaPagamentos(List<FormaPagamento> formaPagamentos) {
		this.formaPagamentos = formaPagamentos;
	}

}
