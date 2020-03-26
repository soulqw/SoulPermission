package com.qw.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.qw.sample.utils.Utils;

import static android.os.Build.VERSION_CODES.M;

public class BeforeActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE_CALL = 100;

    private static final int PERMISSION_CODE_CONTACT = 101;

    private static final int PERMISSION_CODE_READ_PHONE_STATE = 102;

    private static final int REQUEST_CODE_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        findViewById(R.id.bt_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall();
            }
        });
        findViewById(R.id.bt_choose_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseContact();
            }
        });
        findViewById(R.id.bt_read_phone_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPhoneStatus();
            }
        });
    }

    public void makeCall() {
        //6.0以下 直接即可拨打
        if (android.os.Build.VERSION.SDK_INT < M) {
            Utils.makeCall(BeforeActivity.this, "10086");
        } else {
            //6.0以上
            if (ContextCompat.checkSelfPermission(BeforeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BeforeActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                        PERMISSION_CODE_CALL);
            } else {
                Utils.makeCall(BeforeActivity.this, "10086");
            }
        }
    }

    public void chooseContact() {
        //6.0以下 直接即可直接选择
        if (android.os.Build.VERSION.SDK_INT < M) {
            Utils.chooseContact(BeforeActivity.this, REQUEST_CODE_CONTACT);
        } else {
            //6.0以上
            if (ContextCompat.checkSelfPermission(BeforeActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BeforeActivity.this, new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSION_CODE_CONTACT);
            } else {
                Utils.chooseContact(BeforeActivity.this, REQUEST_CODE_CONTACT);
            }
        }
    }

    /**
     * 读取联系人权限和打电话是一组，只要一个授予即可无需重复请求
     */
    public void readPhoneStatus() {
        //6.0以下 直接即可直接读取
        if (android.os.Build.VERSION.SDK_INT < M) {
            Utils.readPhoneStatus(BeforeActivity.this);
        } else {
            //6.0以上
            if (ContextCompat.checkSelfPermission(BeforeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BeforeActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSION_CODE_READ_PHONE_STATE);
            } else {
                Utils.readPhoneStatus(BeforeActivity.this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE_CALL:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(BeforeActivity.this, "本次拨打电话授权失败,请手动去设置页打开权限，或者重试授权权限", Toast.LENGTH_SHORT).show();
                } else {
                    Utils.makeCall(BeforeActivity.this, "10086");
                }
                break;
            case PERMISSION_CODE_CONTACT:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(BeforeActivity.this, "本次选择联系人授权失败,请手动去设置页打开权限，或者重试授权权限", Toast.LENGTH_SHORT).show();
                } else {
                    Utils.chooseContact(BeforeActivity.this, REQUEST_CODE_CONTACT);
                }
                break;
            case PERMISSION_CODE_READ_PHONE_STATE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(BeforeActivity.this, "本次读取联系人失败，请手动去设置页打开权限，或者重试授权权限", Toast.LENGTH_SHORT).show();
                } else {
                    Utils.readPhoneStatus(BeforeActivity.this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CONTACT:
                    Utils.onGetChooseContactData(BeforeActivity.this, data, new Utils.ReadContactListener() {
                        @Override
                        public void onSuccess(Utils.ContactInfo contactInfo) {
                            Toast.makeText(BeforeActivity.this, contactInfo.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}
