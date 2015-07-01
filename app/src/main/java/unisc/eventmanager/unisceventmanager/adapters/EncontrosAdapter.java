package unisc.eventmanager.unisceventmanager.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.MaintenanceEncontroActivity;
import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.fragments.IRefreshFragment;
import unisc.eventmanager.unisceventmanager.qrcode.Contents;
import unisc.eventmanager.unisceventmanager.qrcode.QrCodeEncoder;


/**
 * Created by FAGNER on 28/03/2015.
 */
public class EncontrosAdapter extends BaseAdapter {

    private final Activity m_Context;
    private final ArrayList<Long> m_EncontrosDeletados;
    private LayoutInflater m_BaseInflater;
    private final ArrayList<EncontroMO> m_BaseList;
    private IRefreshFragment RefreshListViewListener;
    private ProgressDialog _progressDialog;
    private File m_FileImage1=null;
    private File m_FileImage2=null;

    public EncontrosAdapter(Activity context, ArrayList<EncontroMO> baseList, ArrayList<Long> encontrosDeletados)
    {
        m_BaseList = baseList;
        m_BaseInflater = LayoutInflater.from(context);
        m_Context = context;
        m_EncontrosDeletados = encontrosDeletados;
    }

    @Override
    public int getCount() {
        return m_BaseList == null ? 0 : m_BaseList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View _view = m_BaseInflater.inflate(R.layout.encontros_adapter_view, null);
        final EncontroMO _encontroTO = m_BaseList.get(position);

        TextView _tvNome = (TextView)_view.findViewById(R.id.adapter_encontro_TvNome);

        _tvNome.setText(_encontroTO.getDescricao());

        ImageButton _btnUpdate = (ImageButton)_view.findViewById(R.id.adapter_encontro_BtnAlterar);
        ImageButton _btnRemove = (ImageButton)_view.findViewById(R.id.adapter_encontro_BtnRemover);

        ImageButton _btnQrCode = (ImageButton)_view.findViewById(R.id.adapter_encontro_BtnQrCode);

        if (_encontroTO.getID() <= 0){
            _btnQrCode.setVisibility(View.GONE);
        }

        _btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask _task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        m_FileImage1 = generateQrCode(String.valueOf(_encontroTO.getID())+"/"+ _encontroTO.getDescricao()+"/"+"E");

                        sendEmail();

                        return null;
                    }
                };

                _task.execute();
            }
        });


        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long _id = Long.valueOf(v.getTag().toString());

                for (EncontroMO _item : m_BaseList)
                {
                    if (_item.getID() == _id)
                    {
                        /*MaintenanceEncontroFragment _mntEnc = new MaintenanceEncontroFragment(null, _item);
                        NavigationManager.Navigate(_mntEnc); */

                        Intent _int = new Intent(m_Context, MaintenanceEncontroActivity.class);
                        _int.putExtra("id", _id);
                        m_Context.startActivity(_int);


                        break;
                    }
                }
            }
        });

        _btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long _id = Long.valueOf(v.getTag().toString());

                for (EncontroMO _item : m_BaseList) {
                    if (_item.getID() == _id) {
                        m_BaseList.remove(_item);


                        break;
                    }

                }

                if (_id > 0) {
                    m_EncontrosDeletados.add(_id);
                }

                if (RefreshListViewListener != null) {
                    RefreshListViewListener.RefreshListView();
                }
            }
        });


        _btnUpdate.setTag(_encontroTO.getID());
        _btnRemove.setTag(_encontroTO.getID());

        return _view;
    }

    public IRefreshFragment getRefreshListViewListener() {
        return RefreshListViewListener;
    }

    public void setRefreshListViewListener(IRefreshFragment refreshListViewListener) {
        RefreshListViewListener = refreshListViewListener;
    }

    private File generateQrCode(final String texto)
    {
        String qrInputText = texto;
        File m_FileImage = null;

        // Find screen size
        WindowManager manager = (WindowManager)m_Context.getSystemService(m_Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QrCodeEncoder qrCodeEncoder = new QrCodeEncoder(qrInputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();


            try {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

//you can create a new file name "test.jpg" in sdcard folder.
                m_FileImage = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "qrcode.jpg");

                if (m_FileImage.exists())
                {
                    m_FileImage.delete();
                }

                m_FileImage.createNewFile();
//write the bytes in file
                FileOutputStream fo = new FileOutputStream(m_FileImage);
                fo.write(bytes.toByteArray());

// remember close de FileOutput
                fo.close();

                m_Context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _progressDialog.cancel();
                    }
                });

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }



        _progressDialog = new ProgressDialog(m_Context);

        m_Context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _progressDialog.setMessage("Gerando QrCode...");
                _progressDialog.show();
            }
        });


        return m_FileImage;
    }

    private void sendEmail()
    {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "fagnerrs@gmail.com");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "dear fagner");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "aula 05");
        emailIntent.setType("application/image");

        if (m_FileImage1 != null) {
            Uri uri1 = Uri.parse("file://" + m_FileImage1);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri1);
        }

        if (m_FileImage2 != null) {
            Uri uri2 = Uri.parse("file://" + m_FileImage2);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri2);
        }
        m_Context.startActivity(emailIntent);
    }

}
