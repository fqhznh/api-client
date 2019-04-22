package com.talkweb.unicom.api.bind;

public interface IBindFactory<T> {

    /**
     * 创建实体
     * @param <T>
     * @return
     */
    public <T> T create();

}
