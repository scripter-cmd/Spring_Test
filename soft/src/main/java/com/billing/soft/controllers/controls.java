package com.billing.soft.controllers;

import java.io.File;

import static org.hibernate.internal.CoreLogging.logger;
import static org.hibernate.internal.HEMLogging.logger;
import org.springframework.ui.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.UUID;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.billing.soft.model.Invoice;
import com.billing.soft.repository.InvoiceRepository;
import com.billing.soft.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class controls {
 private static final Logger logger = LoggerFactory.getLogger(controls.class);
    private static final String DIRECTORY_PATH = "soft/src/main/resources/static/invoices/";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String mobileNumber;
    private String name;
    private String jsonData;
    private String fileName;
    private File jsonFile;
    private String fileadd;

    private final InvoiceService invoiceService; 

    @Autowired
    public controls(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/home")
    public String start() {
        return "index";
    }

    public String fun(InvoiceRequest invoiceData) {
        mobileNumber = invoiceData.getMobileNumber();
        name = invoiceData.getCustomerName();
        jsonData = invoiceData.getJsonData().replaceAll("\\\\", "").replaceAll("\\$", "");
        
        
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

      
        fileadd = UUID.randomUUID() + mobileNumber;
        fileName = DIRECTORY_PATH + fileadd + ".json";
        jsonFile = new File(fileName);

        try {
           
            objectMapper.writeValue(jsonFile, jsonData);

           
            logger.info("Invoice data saved successfully for mobile number -> {}", mobileNumber);
            logger.info("Check the file path -> {}", fileName);
            logger.info("Response -> {}", jsonData);
            System.out.println("data: " + jsonData);
            System.out.println("Name: " + name);
            System.out.println("Number: " + mobileNumber);
            
           
            invoiceService.saveInvoice(mobileNumber, name, fileadd);

            return "{\"status\":\"success\", \"message\":\"Invoice saved for mobile number: " + mobileNumber + "\"}";
        } catch (IOException e) {
            logger.error("Error saving invoice data for mobile number: {}", mobileNumber, e);
            return "{\"status\":\"error\", \"message\":\"Failed to save invoice\"}";
        }
    }

    @Autowired
    private InvoiceRepository invoiceRepository;  
    
    @PostMapping("/saveInvoice")
    public ResponseEntity<String> saveInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        try {
            
            Invoice invoice = new Invoice();
            invoice.setMobileNumber(invoiceRequest.getMobileNumber());
            invoice.setCustomerName(invoiceRequest.getCustomerName());
            invoice.setJsonData(invoiceRequest.getJsonData());  

            invoiceRepository.save(invoice); 

            return ResponseEntity.ok("Invoice saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving invoice");
        }
    }

    @PostMapping("/saveDb")
    @ResponseBody
    public String saveDb(@RequestBody InvoiceRequest invoiceData, Model model) {
        System.out.println("Save Db Call");
        String str = fun(invoiceData);
        System.out.println(str);
        return "{\"status\":\"success\", \"message\":\"Invoice saved for mobile number: " + mobileNumber + "\"}";
    }

    @GetMapping("/save")
    public String Loadpg() {
        return "save";
    }

    @GetMapping("/bill")
    public String bill(Model model) {
        System.out.println("File ->" + fileName);
        model.addAttribute("jsonData", fileadd);
        System.out.println(model.getAttribute("filePath"));
        return "bill";
    }
}
