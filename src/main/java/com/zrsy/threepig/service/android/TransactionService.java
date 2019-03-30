package com.zrsy.threepig.service.android;

import com.zrsy.threepig.domain.ParserResult;

public interface TransactionService {
    ParserResult getBanlanceOf(String address);
}
