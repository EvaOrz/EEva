package modernmedia.com.cn.corelib.webridge;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * uri分发机制
 *
 * @author user
 */
public class WBUri {
    private Context mContext;
    private WebUriListener mListener;

    public WBUri(Context context, WebUriListener listener) {
        mContext = context;
        mListener = listener;
        if (mContext == null) {
            throw new NullPointerException("WBUri mContext is NULL!");
        }
        if (mListener == null) {
            throw new NullPointerException("WBUri UriListener is NULL!");
        }
    }

    /**
     * 是否可以用slate协议打开uri
     *
     * @param uriStr
     * @return
     */
    public boolean canOpenURI(String uriStr) {
        if (TextUtils.isEmpty(uriStr)) {
            return false;
        }

        Uri uri = Uri.parse(uriStr);
        String scheme = uri.getScheme();
        String path = uri.getPath();
        if (TextUtils.isEmpty(scheme) || TextUtils.isEmpty(path)) {
            return false;
        }

        if (scheme.equals("slate")) {
            //            UriParse.clickSlate(mContext, uriStr, new Entry[]{new ArticleItem()}, null, new Class<?>[0]);
            return false;
        }
        Log.e("未能识别的uri  --1 ", uriStr);
        return false;
    }

    private boolean handleURICommand(String command, String params, List<String> paramsList) {
        String methodName = command + "Command";
        Object[] args = {command, params, paramsList};
        try {
            InvokeMethod.invokeMethod(mListener, methodName, args);
        } catch (Exception e) {
            e.printStackTrace();
            // 弹出dialog提示异常信息
            //            mListener.unknownCommand(command, params, paramsList);
            return false;
        }

        return true;
    }

}
