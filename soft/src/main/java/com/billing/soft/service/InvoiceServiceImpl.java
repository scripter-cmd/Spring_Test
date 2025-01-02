package com.billing.soft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.soft.model.Invoice;
import com.billing.soft.repository.InvoiceRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService {
     private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void saveInvoice(String mobileNumber, String name, String fileName) {
        Invoice invoice = new Invoice();
        invoice.setMobileNumber(mobileNumber);  // Save mobile number
        invoice.setCustomerName(name);  // Save name
        invoice.setJsonData(""+fileName);

        // Save the invoice to the database
        invoiceRepository.save(invoice);
    }
}
