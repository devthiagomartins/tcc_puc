package br.com.projetotcc.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;


@ManagedBean(name = "ExportarTabelaMB")
@ViewScoped
public class ExportarTabelaBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Gerar XLS
	public void gerarXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColorPredefined.GREEN.getIndex());

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
    }

	//Gerar PDF
    public void gerarPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);
        
        Font catFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLD);

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "demo/images/pdf.png";
        
        
        
        Paragraph p = new Paragraph("Relatório", catFont);
        p.setSpacingAfter(10f);

        p.setAlignment(Element.ALIGN_CENTER);

        pdf.add(Image.getInstance(logo));
        
        pdf.add(p);

        
    }
	   
	     
	}