package cn.com.modernmedia.corelib.listener;

/**
 * 解析完JSON数据之后的回调函数
 * 
 * @author ZhuQiao
 * 
 */
public interface DataCallBack {
	public void callback(boolean success, boolean fromHttp);
}
