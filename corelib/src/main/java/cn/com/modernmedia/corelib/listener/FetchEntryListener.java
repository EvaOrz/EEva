package cn.com.modernmedia.corelib.listener;


import cn.com.modernmedia.corelib.model.Entry;

/**
 * 所有自定义view必须实现此接口,调用接口成功后实现此接口
 */
public interface FetchEntryListener {
    /**
     * 给View传递数据
     *
     * @param entry
     */
    public void setData(Entry entry);
}
