package com.zrsy.threepig.service.android;

import com.sun.mail.imap.protocol.ID;
import com.zrsy.threepig.domain.ParserResult;
import org.springframework.web.bind.annotation.PathVariable;

public interface TransactionService {
    ParserResult getBanlanceOf(String address);

    ParserResult getPigInfo(String earId);

    ParserResult confirmBuy(String address, String id);

    ParserResult transfer(String fromAddress,String toAddress,String id);

    ParserResult changeStatus(String fromAddress,String toAddress,String id);
}
