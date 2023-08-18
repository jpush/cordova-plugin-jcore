package cn.jiguang.cordova.jcore;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Iterator;
import cn.jiguang.api.utils.JCollectionAuth;
import cn.jiguang.api.JCoreInterface;
public class JCorePlugin extends CordovaPlugin {
    private static final String TAG = "JCorePlugin";
    private Context mContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mContext = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "execute:" + action);
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Method method = JCorePlugin.class.getDeclaredMethod(action, JSONArray.class,
                            CallbackContext.class);
                    method.invoke(JCorePlugin.this, args, callbackContext);
                } catch (Throwable e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
        });
        return true;
    }

    void testCountryCode(JSONArray jsonArray,CallbackContext callbackContext){
        Log.d(TAG, "testCountryCode jsonArray:" + jsonArray);

        try {
           String code = jsonArray.getString(0);
            Log.d(TAG, "testCountryCode code:" + code);

            JCoreInterface.testCountryCode(code);
        }catch (Throwable throwable){
            Log.d(TAG, "testCountryCode throwable:" + throwable);

        }
    }
    void setAuth(JSONArray jsonArray,CallbackContext callbackContext){
        Log.d(TAG, "setAuth jsonArray:" + jsonArray);

        try {
           boolean enable = jsonArray.getBoolean(0);
            Log.d(TAG, "setAuth enable:" + enable);

            JCollectionAuth.setAuth(mContext,enable);
        }catch (Throwable throwable){
            Log.d(TAG, "setAuth throwable:" + throwable);

        }
    }
    void enableAutoWakeup(JSONArray jsonArray,CallbackContext callbackContext){
        Log.d(TAG, "enableAutoWakeup jsonArray:" + jsonArray);

        try {
           boolean enable = jsonArray.getBoolean(0);
            Log.d(TAG, "enableAutoWakeup enable:" + enable);

            JCollectionAuth.enableAutoWakeup(mContext,enable);
        }catch (Throwable throwable){
            Log.d(TAG, "enableAutoWakeup throwable:" + throwable);

        }
    }
}
