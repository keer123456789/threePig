package com.zrsy.threepig.initializer;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import net.i2p.crypto.eddsa.EdDSASecurityProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.security.Security;

@Component
public class BigchaindbSetupRunner implements CommandLineRunner {
    @Value("${blockchaindb.base-url}")
    private String baseUrl;

    @Override
    public void run(String... args) throws Exception {
        BigchainDbConfigBuilder
                .baseUrl(baseUrl)
                .setup();
    }
}
