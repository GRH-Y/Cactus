package com.yyz.ard.cactus.network.joggle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public interface ISSLFactory {
    
    SSLSocketFactory getSSLSocketFactory();

    HostnameVerifier getHostnameVerifier();
}
