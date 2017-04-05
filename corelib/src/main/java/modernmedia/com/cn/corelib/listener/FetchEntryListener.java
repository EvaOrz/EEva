package modernmedia.com.cn.corelib.listener;


import modernmedia.com.cn.corelib.model.Entry;

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
