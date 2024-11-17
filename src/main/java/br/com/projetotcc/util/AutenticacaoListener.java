package br.com.projetotcc.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Faces;

import br.com.projetotcc.bean.AutenticacaoBean;
import br.com.projetotcc.domain.Usuario;

@SuppressWarnings("serial")
public class AutenticacaoListener implements PhaseListener{

	@Override
	public void afterPhase(PhaseEvent event) {
		
		
		String paginaAtual = Faces.getViewId();
		
		boolean isPaginaAutenticacao = paginaAtual.contains("login.xhtml");
		
		if(!isPaginaAutenticacao) {
			AutenticacaoBean autenticacaoMB =  Faces.getSessionAttribute("AutenticacaoMB");
			
			if(autenticacaoMB == null) {
				Faces.navigate("login.xhtml");
				return;
			}
			
			Usuario usuario = autenticacaoMB.getUsuarioLogado();
			if(usuario == null) {
				Faces.navigate("login.xhtml");
				return;
			}
		}
		
	
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		
		
	}

	@Override
	public PhaseId getPhaseId() {  
		
		return PhaseId.ANY_PHASE;
	}

}
