/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.formbuilder.model;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.qlc.fieldsense.formbuilder.model.*;
import static com.qlc.fieldsense.team.dao.TeamDaoImpl.log4jLog;
import com.qlc.fieldsense.utils.Constant;
/**
 *
 * @author Awaneesh
 */
public class XMLConverter {
    
    /**
     *
     * @param form
     * @param accountId
     * @return
     */
    public boolean generateFormXml(CustomForm form , int accountId)
{
	  try {
                /*
                CustomForm form =new CustomForm();
                form.setId(1);
                form.setActive(true);
                form.setFormName("form1");
                form.setMobileOnly(true);
                form.setSubmit_caption("Submit");
                form.setForm_related_to("Visit");
                form.setForm_trigerred_event("On Demand");
                
                java.util.ArrayList<FormFields> tableData =new java.util.ArrayList<FormFields>();
                FormFields field= new FormFields();
                field.setFieldId(1);
                field.setEditField("0");
                field.setFieldOrder("1");
                field.setFieldType("Textbox");
                field.setForm_id_fk("1");
                field.setLabelName("Name1");
                field.setMandField("0");
                field.setPlaceHolder("your name");
                field.setOption("");
                tableData.add(field);
                
                field= new FormFields();
                field.setFieldId(2);
                field.setEditField("0");
                field.setFieldOrder("1");
                field.setFieldType("Dropdown");
                field.setForm_id_fk("1");
                field.setLabelName("Country");
                field.setMandField("0");
                field.setPlaceHolder("your country");
                field.setOption("India\nPakistan\nJapan\nRussia");
                tableData.add(field);
                
                form.setTableData(tableData);
                */
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("fsform");
                Attr attr = doc.createAttribute("id");
		attr.setValue(String.valueOf(form.getId()));
		rootElement.setAttributeNode(attr);
                
                attr = doc.createAttribute("name");
		attr.setValue(form.getFormName());
		rootElement.setAttributeNode(attr);
                
                attr = doc.createAttribute("relatedto");
		attr.setValue(form.getForm_related_to());
		rootElement.setAttributeNode(attr);
                
                attr = doc.createAttribute("whentobefilled");
		attr.setValue(form.getForm_trigerred_event());
		rootElement.setAttributeNode(attr);
                
                attr = doc.createAttribute("submitcaption");
		attr.setValue(form.getSubmit_caption());
		rootElement.setAttributeNode(attr);
                


		doc.appendChild(rootElement);

                
                java.util.ArrayList<FormFields> fieldList= form.getTableData();

                for(FormFields formField: fieldList){
                    
                    Element fieldTag = doc.createElement("field");

                    attr = doc.createAttribute("id");
                    attr.setValue(String.valueOf(formField.getFieldId()));
                    fieldTag.setAttributeNode(attr);

                    attr = doc.createAttribute("name");
                    attr.setValue(formField.getLabelName());
                    fieldTag.setAttributeNode(attr);
                    
                    attr = doc.createAttribute("datatype");
                    attr.setValue(formField.getFieldType());
                    fieldTag.setAttributeNode(attr);

                    attr = doc.createAttribute("hint");
                    attr.setValue(formField.getPlaceHolder());
                    fieldTag.setAttributeNode(attr);

                    attr = doc.createAttribute("minlimit");
                    attr.setValue("0");
                    fieldTag.setAttributeNode(attr);

                    attr = doc.createAttribute("maxlimit");
                    attr.setValue("255");
                    fieldTag.setAttributeNode(attr);

                    attr = doc.createAttribute("required");
                    attr.setValue(String.valueOf(formField.getMandField()));
                    fieldTag.setAttributeNode(attr);

                    attr = doc.createAttribute("editable");
                    attr.setValue(String.valueOf(formField.getEditField()));
                    fieldTag.setAttributeNode(attr);
                    
                    attr = doc.createAttribute("geoenable");
                    attr.setValue(String.valueOf(formField.getGeoTagEnable()));
                    fieldTag.setAttributeNode(attr);

                       attr = doc.createAttribute("listType");
                    attr.setValue(formField.getListType());
                    fieldTag.setAttributeNode(attr);
                    
                    attr = doc.createAttribute("listName");
                    attr.setValue(formField.getListName());
                    fieldTag.setAttributeNode(attr);

                    fieldTag.appendChild(doc.createTextNode(formField.getLabelName()));

                    rootElement.appendChild(fieldTag);

                    if(formField.getFieldType().equalsIgnoreCase("Dropdown") ||formField.getFieldType().equalsIgnoreCase("Radio") || formField.getFieldType().equalsIgnoreCase("Checkboxes") ||formField.getFieldType().equalsIgnoreCase("Predefined Lists")){
                        String[] options= formField.getOption().split("\n");
                        for(int i=0;i<options.length;i++){
                         Element option = doc.createElement("option");
                         
                         attr = doc.createAttribute("value");
                         attr.setValue(options[i]);
                         option.setAttributeNode(attr);
                         option.appendChild(doc.createTextNode(options[i]));
                        
                         fieldTag.appendChild(option);  
                        }
                    }else{
                        Element option = doc.createElement("option");
                        fieldTag.appendChild(option);
                    }
                
                }


		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty( javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty( javax.xml.transform.OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(Constant.CUSTOM_FORM_XML_UPLOAD_PATH+"account_"+accountId+"_form_"+form.getId()+".xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

	//	System.out.println("File saved!");

	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
                return false;
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
                return false;
	  }
    return true;
}

    /**
     *
     * @param formId
     * @param accountId
     * @return
     */
    public boolean deleteXMLFile (int formId, int accountId){
    try{
    		
    		File file = new File(Constant.CUSTOM_FORM_XML_UPLOAD_PATH+"account_"+accountId+"_form_"+formId+".xml");
        	
    		if(file.delete()){
    	//		System.out.println(file.getName() + " is deleted!");
                        return true;
    		}else{
    	//		System.out.println("Delete operation is failed.");
                        return false;
    		}
    	   
    	}catch(Exception e){
    		log4jLog.info(" deleteXMLFile " + e);
//    		e.printStackTrace();
                return false;
    		
    	}
}
}