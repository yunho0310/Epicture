package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public abstract class DB_Connect extends AsyncTask<String, Void, String> implements CallbackReceiver {

    Context context;
    ProgressDialog asyncDialog=null;
    Boolean has_dialog;
    public DB_Connect(Context _context, Boolean has_progress_dialog) {
        has_dialog = has_progress_dialog;
        context = _context;
        asyncDialog = new ProgressDialog(context);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("DB 작업중입니다.");
        asyncDialog.setCanceledOnTouchOutside(false);
    }

    public abstract void receiveData(Object object);
    @Override
    protected void onPreExecute() {
        if(has_dialog)
            asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String _content_type=null, _send_data=null, _file_path=null, _method, _token, _url;
        _url = params[0];
        if(params.length == 5) { //jpg 보내기
            _content_type = params[4];
            _file_path = params[1];
        }
        else //json 보내기
            _send_data = params[1];
        _method = params[2];
        _token = params[3];
        System.out.println("in back, url="+_url);
        System.out.println("in back, send_data="+_send_data);
        try {
            URL url = new URL(_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if(_content_type == null)
                con.setRequestProperty("Content-Type", "application/json");
            else
                con.setRequestProperty("Content-Type", _content_type);
            con.setRequestMethod(_method);
            con.setConnectTimeout(5000);

            if (_method.equals("POST") || _method.equals("PUT") || _method.equals("PATCH")) {
                if(_send_data != null) { //이거 없으면 put인데 보내는 데이터 없을때 안 보내짐;;;
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream outs = con.getOutputStream();
                    if (_content_type == null) {//json
                        outs.write(_send_data.getBytes("UTF-8"));
                    } else { //jpg
                        Bitmap bitmap = BitmapFactory.decodeFile(_file_path);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outs);
                    }
                    outs.flush();
                    outs.close();
                }
            }

            StringBuilder sb = new StringBuilder();
            InputStream inputs = null;
            BufferedReader bufferedReader = null;

            /********** 서버에서 404를 보낼때 exception error 나는 문제 해결****************/
            int statusCode = con.getResponseCode();
            if (statusCode >= 200 && statusCode < 400) {
                inputs = con.getInputStream();
            }
            else {
                inputs = con.getErrorStream();
            }
            /*****************************************************************************/
            bufferedReader = new BufferedReader(new InputStreamReader(inputs));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if(result == null) {
            Toast.makeText(context.getApplicationContext(), "result null in DB_Connect", Toast.LENGTH_SHORT).show();
        }
        else {
            receiveData(result);
        }
        if (asyncDialog != null || asyncDialog.isShowing()){
            asyncDialog.dismiss();
        }
        super.onPostExecute(result);
    }
}
