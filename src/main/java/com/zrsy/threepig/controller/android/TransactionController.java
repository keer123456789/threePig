package com.zrsy.threepig.controller.android;

import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.android.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private Logger logger= LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;

    @GetMapping("/getBalanceOf/{address}")
    public ParserResult getBanlanceOf(@PathVariable String address){
       return transactionService.getBanlanceOf(address);
    }
}
