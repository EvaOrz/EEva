package modernmedia.com.cn.corelib;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Eva. on 17/3/17.
 */

public class BaseActivity extends Activity {


    public void showToast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();

    }

    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

}
