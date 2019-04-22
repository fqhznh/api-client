package com.talkweb.unicom.api.wrapper;

import java.util.List;

public interface IResultWrapper {

    public <T> T wrapper(String data, Class<T> clazz);

    public <T> List<T> wrapperList(String data, Class<T> clazz);

}
