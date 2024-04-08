package com.cars4u.sample.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cars4u.sample.common.CustomUtils;
import com.cars4u.sample.common.EncryptionUtils;
import com.cars4u.sample.entity.Info;
import com.cars4u.sample.service.impl.InfoService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;


@Controller
@RequestMapping("final")
public class FinalController {
	
	@Autowired
	private InfoService infoService;
	
	@Value("${files.vehicle.temp}")
	String uploadsTemp;
	
	private Style headerStyle;
	private Style contentStyle;
	private PdfFont fontNormal;
	private PdfFont fontBold;
	
	@RequestMapping(value = "generatePdf" , method = {RequestMethod.GET , RequestMethod.POST})
	public ResponseEntity<InputStreamResource> generatePdf(@RequestParam(required = false) String id) throws IOException {
		
		Long infoId = EncryptionUtils.decrypt(id);
		if(infoId != null && infoId > 0 ) {
			Info info = infoService.getDetailsById(infoId);
			if(info != null && info.getId() != null) {

				Path destinationPath = null;
				String fileName = info.getName();
				final String documentFormat = "pdf";
				fileName += "." + documentFormat;
				
				SimpleDateFormat dateFormatForFolder = new SimpleDateFormat("dd-MM-yyyy");
				String dateAndTimeForFolder = dateFormatForFolder.format(new Date());
				
				destinationPath = Paths.get(uploadsTemp + dateAndTimeForFolder);
				if (destinationPath.toFile().exists() == false) {
					Files.createDirectories(destinationPath);
				}
				Path baseFilePath = Paths.get(destinationPath + File.separator + fileName);
				try (FileOutputStream outputStream = new FileOutputStream(baseFilePath.toString());
						PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
						Document document = new Document(pdfDocument, PageSize.A4, false);) {
					pdfDocument.addNewPage();
					document.setMargins(0, 0, 0, 0);

					fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
					fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

					headerStyle = new Style();
					headerStyle.setFontSize(8);
					headerStyle.setBackgroundColor(ColorConstants.LIGHT_GRAY);
					headerStyle.setFontFamily(StandardFonts.HELVETICA_BOLD);
					
					contentStyle = new Style();
					contentStyle.setFontSize(8);
					contentStyle.setFontFamily(StandardFonts.HELVETICA);
					contentStyle.setFontColor(ColorConstants.BLACK);
					
					addHeader(document , info);
					
					Date date = info.getDate();
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					
					int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
					
					String dayOfWeekString;
					switch (dayOfWeek) {
					    case Calendar.SUNDAY:
					        dayOfWeekString = "Sunday";
					        break;
					    case Calendar.MONDAY:
					        dayOfWeekString = "Monday";
					        break;
					    case Calendar.TUESDAY:
					        dayOfWeekString = "Tuesday";
					        break;
					    case Calendar.WEDNESDAY:
					        dayOfWeekString = "Wednesday";
					        break;
					    case Calendar.THURSDAY:
					        dayOfWeekString = "Thursday";
					        break;
					    case Calendar.FRIDAY:
					        dayOfWeekString = "Friday";
					        break;
					    case Calendar.SATURDAY:
					        dayOfWeekString = "Saturday";
					        break;
					    default:
					        dayOfWeekString = "Unknown";
					        break;
					}
					
					addFooter(document , info , dayOfWeekString);
					
					document.close();

					/* SETUP-RESPONSE */
					File responseFile = baseFilePath.toFile();
					MediaType mediaType = MediaType.parseMediaType("application/pdf");
					InputStreamResource resource = new InputStreamResource(new FileInputStream(responseFile));

					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
							.contentType(mediaType).contentLength(responseFile.length()).body(resource);
									
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
				} finally {
					CustomUtils.removeFile(destinationPath.toString());
				}
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		
	}

	private void addHeader(Document document, Info info) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		float[] pointColumnWidths = { (float) 50, (float) 50};
		Table table = new Table(UnitValue.createPercentArray(pointColumnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		table.setPadding(2);
		table.setMarginTop(30);
		table.setMarginLeft(30);
		table.setMarginRight(30);
		table.setMarginBottom(5);
		table.setBorder(Border.NO_BORDER);
		table.setKeepTogether(true);
		
		int commonfontsize = 12;
	//	StringBuilder builder;
		String tempString;
		Cell cell;
		
		cell = new Cell(0, 1);
		tempString = "Cars4U";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontBold);
		cell.setPaddingBottom(6);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorderLeft(Border.NO_BORDER);
		cell.setBorderTop(Border.NO_BORDER);
		cell.setBorderRight(Border.NO_BORDER);
		table.addCell(cell);
		
		
		cell = new Cell(1, 2);
		if(info.getSequenceNo() != null) {
			tempString = info.getSequenceNo() + " dated " + dateFormat.format(info.getSequenceDate());
			cell.add(new Paragraph(tempString));
		} else {
			tempString = "DRAFT COPY";
			cell.add(new Paragraph(tempString));
			cell.setFontColor(ColorConstants.LIGHT_GRAY);
		}
		cell.setFont(fontBold);
		cell.setPaddingBottom(6);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.RIGHT);
		cell.setBorderLeft(Border.NO_BORDER);
		cell.setBorderTop(Border.NO_BORDER);
		cell.setBorderRight(Border.NO_BORDER);
		table.addCell(cell);
		
		document.add(table);
		
		
	}
	
	private void addFooter(Document document, Info info , String dayOfWeekString) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		float[] pointColumnWidths = { (float) 15, (float) 16, (float) 17, (float) 10, (float) 10, (float) 16,(float) 16 };
		Table table = new Table(UnitValue.createPercentArray(pointColumnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		table.setPadding(2);
		table.setMarginTop(5);
		table.setMarginLeft(30);
		table.setMarginRight(30);
		table.setMarginBottom(30);
		table.setBorder(Border.NO_BORDER);
		table.setKeepTogether(true);

		String tempString;
		Cell cell;

		int commonfontsize = 11;
		
		cell = new Cell(1, 2);
		tempString = "Form No. "+info.getId();
		cell.add(new Paragraph(tempString));
		cell.setFont(fontBold);
		cell.setFontSize(12);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
	
		cell = new Cell(1, 3);
		tempString = "Appointment Form";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontBold);
		cell.setFontSize(12);
		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(1, 2);
		tempString = "";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.RIGHT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(1, 7);
		tempString = "\n "+" "+" Dear "+info.getName()+" ,";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontBold);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
//		cell = new Cell(0, 1);
//		tempString = "";
//		cell.add(new Paragraph(tempString));
//		cell.setFont(fontNormal);
//		cell.setFontSize(commonfontsize);
//		cell.setTextAlignment(TextAlignment.RIGHT);
//		cell.setBorder(Border.NO_BORDER);
//		table.addCell(cell);
		
		cell = new Cell(3, 7);
		tempString = "            We are pleased to inform you that your vehicle service appointment has been successfully confirmed at our esteemed service center."
				+ " Your commitment to maintaining your vehicle's health and safety is greatly appreciated, and we are honored to be entrusted with its care.";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(2, 7);
//		tempString = "\nYour service has been scheduled for <b>" +dateFormat.format(info.getDate()) +"</b> . Upon your arrival, please present the following token number : <b>"+info.getSequenceNo()+"</b>  ."
//				+ " This will ensure a smooth and efficient check-in process, allowing us to attend to your vehicle promptly.";
		String scheduledText = "\nYour service has been scheduled for ";
        String boldDate = dateFormat.format(info.getDate()) +","+ dayOfWeekString; 
        String tokenText = ". Upon your arrival, please present the following token number: ";
        String boldToken = info.getSequenceNo();
        String remainingText = ". This will ensure a smooth and efficient check-in process, allowing us to attend to your vehicle promptly.";
        
        Paragraph paragraph = new Paragraph();
        paragraph.add(scheduledText);
        paragraph.add(new Text(boldDate).setFont(fontBold));
        paragraph.add(tokenText);
        paragraph.add(new Text(boldToken).setFont(fontBold));
        paragraph.add(remainingText);
	
		cell.add(paragraph);
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(2, 7);
		tempString = "\nWe understand the importance of reliable transportation in your daily life, and our team is dedicated to providing you with top-notch service to keep your vehicle running smoothly. "
				+ "From routine maintenance to complex repairs, your satisfaction and safety are our highest priorities.";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(2, 7);
		tempString = "\nOnce again, we extend our heartfelt gratitude for choosing our service center. We look forward to serving you and your vehicle with the utmost care and attention to detail.";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(2, 7);
		tempString = "\nShould you have any questions or require further assistance, please do not hesitate to contact us. Thank you for your continued trust in us.";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		
		cell = new Cell(1, 7);
		tempString = "\nBest Regards,";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(1, 5);
		tempString = "Cars4U Team";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
		
		cell = new Cell(1, 2);
		tempString = "Contact Us:\n+91-9448805533\ncars4U2017@gmail.com";
		cell.add(new Paragraph(tempString));
		cell.setFont(fontNormal);
		cell.setFontSize(commonfontsize);
		cell.setTextAlignment(TextAlignment.RIGHT);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);

		document.add(table);	
	}

}
