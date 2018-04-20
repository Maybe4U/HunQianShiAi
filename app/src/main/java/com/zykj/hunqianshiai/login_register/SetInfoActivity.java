package com.zykj.hunqianshiai.login_register;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.select_city.SelectCityActivity;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 完善基本资料
 */
public class SetInfoActivity extends BasesActivity implements android.app.DatePickerDialog.OnDateSetListener, BaseView<String> {

    @Bind(R.id.et_username)
    EditText username;
    @Bind(R.id.rl_sex)
    RelativeLayout mRelativeLayout1;
    @Bind(R.id.rl_birthday)
    RelativeLayout mRelativeLayout2;
    @Bind(R.id.rl_work_city)
    RelativeLayout mRelativeLayout3;
    @Bind(R.id.rl_income)
    RelativeLayout mRelativeLayout4;
    @Bind(R.id.tv_birthday)
    TextView birthday;
    @Bind(R.id.tv_sex)
    TextView sex;
    @Bind(R.id.tv_income)
    TextView tvIncome;
    @Bind(R.id.tv_city)
    TextView tvCity;

    private String[] sexArray = new String[]{"男", "女"};// 性别选择

    List<String> incomeList;
    private OptionsPickerView mPickerView;
    private BasePresenterImpl mPresenter;
    private String mCity_id;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_set_info;
    }

    @Override
    protected void initView() {
        appBar("完善基本资料");
        String[] incomes = getResources().getStringArray(R.array.incomes);
        incomeList = Arrays.asList(incomes);
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @OnClick({R.id.tv_next, R.id.rl_birthday, R.id.rl_work_city, R.id.rl_income, R.id.rl_sex})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_next:
//                openActivity(CertificationActivity.class);
                String nick = username.getText().toString().trim();
                if (nick.length() <= 0 && nick.length() > 8) {
                    toastShow("昵称不正确");
                    return;
                }
                String s = sex.getText().toString();
                if (s.equals("请选择")) {
                    toastShow("请选择性别");
                    return;
                }

                String s1 = birthday.getText().toString();
                if (s1.equals("请选择")) {
                    toastShow("请选择生日");
                    return;
                }
                String s2 = tvIncome.getText().toString();
                if (s2.equals("请选择")) {
                    toastShow("请选择年收入");
                    return;
                }

                String s3 = tvCity.getText().toString();
                if (s3.equals("请选择")) {
                    toastShow("请选择工作生活地点");
                    return;
                }

                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("p1", nick);
                mParams.put("p2", s);
                mParams.put("p6", s1);
                mParams.put("p4", s2);
                mParams.put("p5", mCity_id);
                mPresenter.getData(UrlContent.COMPLETE_INFO_URL, mParams, BaseModel.DEFAULT_TYPE);

                break;
            case R.id.rl_sex://设置性别
                AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setSingleChoiceItems(sexArray, 0, new DialogInterface.OnClickListener() {// 2默认的选中

                    @Override
                    public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                        // showToast(which+"");
                        sex.setText(sexArray[which]);
                        dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
                break;
            case R.id.rl_birthday://设置生日
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, this, year, month, day).show();
                break;
            case R.id.rl_work_city://设置城市
                Intent intent = new Intent(this, SelectCityActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.rl_income://设置收入
                mPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tvIncome.setText(incomeList.get(options1));

                    }
                }).build();

                mPickerView.setPicker(incomeList);
                mPickerView.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        birthday.setText(i + "." + (i1 + 1) + "." + i2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 102) {
            mCity_id = data.getStringExtra("city_id");
            String city = data.getStringExtra("city");
            tvCity.setText(city);
        }
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code == 200) {
            openActivity(CertificationActivity.class);
        } else {
            toastShow("保存不成功");
        }

    }

    @Override
    public void refresh(String bean) {

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }
}
