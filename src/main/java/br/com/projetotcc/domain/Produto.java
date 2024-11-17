package br.com.projetotcc.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Produto extends GenericDomain{

	@Column(length = 100, nullable = false)
	private String descricao;
	
	@Column(length = 100)
	private String nomeFantasia;
	
	@Column(nullable = false)
	private Short quantidade;
	
	
	@Column
	private String contraIndicacoes;
	
	@Column(nullable = false, precision = 6, scale = 2)  // precision é quantas casas no total o valor tem , scale sao quantas casas tem apos a virgula . 
	private BigDecimal preco; 	//bigDecimal trata melhor valores monetarios, com calculos com dizimas e tem tipo primitivo = null , e nao 0.
	
	
	@Column(nullable = false)
	private Date dataValidade;
	
	@Column(nullable = false)
	private Date dataCadastro;
	
	@Column(nullable = false)
	private boolean isAtivo;
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private Fabricante fabricante;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Categoria categoria;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Short getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Short quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	


	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getContraIndicacoes() {
		return contraIndicacoes;
	}

	public void setContraIndicacoes(String contraIndicacoes) {
		this.contraIndicacoes = contraIndicacoes;
	}


	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	
	
	public boolean isAtivo() {
		return isAtivo;
	}

	public void setAtivo(boolean isAtivo) {
		this.isAtivo = isAtivo;
	}

	
}
