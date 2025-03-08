// IService.aidl
package com.senai.grupo11.biscoitodasorte;

import com.senai.grupo11.biscoitodasorte.ICallback;

interface IService {
    void registerCallback(ICallback cb);
    void removeCallback(ICallback cb);
    void requestText();
}