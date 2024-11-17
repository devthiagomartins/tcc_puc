package br.com.projetotcc.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
public class Funcionario extends GenericDomain{
	
	@Column(length = 15, nullable = false)
	private String carteiraTrabalho;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataAdmissao;
	
	@JoinColumn(nullable = false)
	@OneToOne
	private Pessoa pessoa;
	
	@Column(nullable = false)
	private Boolean ativo;
	
	public String getCarteiraTrabalho() {
		return carteiraTrabalho;
	}

	public void setCarteiraTrabalho(String carteiraTrabalho) {
		this.carteiraTrabalho = carteiraTrabalho;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}


	public Boolean getAtivo() {
		return ativo;
	}
	
	@Transient
	public String getAtivoString() {
		String ativoString = null;
		if(ativo) {
			ativoString = "Ativo";
		}else if(ativo == false) {
			ativoString = "Inativo";
		}
		return ativoString;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	
	
}
