package br.com.projetotcc.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.projetotcc.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;



public class ModeloRelatorio {
	    
	    private HttpServletResponse response;
	    private FacesContext context;
	    private ByteArrayOutputStream baos;
	    private InputStream stream;
    

	    public ModeloRelatorio() {
	    
	    	
	    	this.context = FacesContext.getCurrentInstance();
	        this.response = (HttpServletResponse) context.getExternalContext().getResponse();
	    }
	    
	    
	    public void getRelatorioPDF() throws Exception{
	        
	    	
	    	 Connection conexao = HibernateUtil.getConexao();
	    	stream = this.getClass().getResourceAsStream("/reports/Produtos.jasper");
	        Map<String, Object> params = new HashMap<String, Object>();
	        
	        baos = new ByteArrayOutputStream();
	        try {
	            
	            JasperReport report = (JasperReport) JRLoader.loadObject(stream);
	            
                JasperPrint print = JasperFillManager.fillReport(report, params, conexao);
	            JasperExportManager.exportReportToPdfStream(print, baos);
	            
	            response.reset();
	            response.setContentType("application/pdf");
	            response.setContentLength(baos.size());
	            response.setHeader("Content-disposition","inline; filename=relatorio.pdf");
	            response.getOutputStream().write(baos.toByteArray());
	            response.getOutputStream().flush();
	            response.getOutputStream().close();
	            
	            context.responseComplete();
	            conexao.close();
	            
	        } 
	        
	        catch (JRException ex) {
	            Logger.getLogger(ModeloRelatorio.class.getName()).log(Level.SEVERE, null, ex);
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
	        } 
	        
	        catch (IOException ex) {
	            Logger.getLogger(ModeloRelatorio.class.getName()).log(Level.SEVERE, null, ex);
	        }
    }   
   
}
